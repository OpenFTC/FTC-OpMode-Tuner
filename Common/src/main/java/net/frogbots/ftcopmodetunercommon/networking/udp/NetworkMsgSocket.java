/*
 * Copyright (c) 2018 FTC team 4634 FROGbots
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.frogbots.ftcopmodetunercommon.networking.udp;

import net.frogbots.ftcopmodetunercommon.misc.CircularCache;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NetworkMsgSocket extends NetworkMsgSocketBase
{
    private ArrayList<NetworkCommand> cmdsWaitingForAck = new ArrayList<>(25);
    private ScheduledExecutorService repeatTxTimer;
    private Runnable repeatTxTimerRunnable;
    private final Object cmdsWaitingForAckLock = new Object();
    private SpecificMsgReceiver callback;
    private CircularCache<NetworkCommand> circularCommandCache = new CircularCache<>(NetworkCommand.class, 25);

    public NetworkMsgSocket()
    {
        super();
        super.setCallback(new GenericReceiver());

    }

    public void setCallback(SpecificMsgReceiver callback)
    {
        this.callback = callback;
    }

    @Override
    public void open(int port)
    {
        super.open(port);

        repeatTxTimerRunnable = new Runnable()
        {
            /*
             * We can't remove anything from the cmdsWaitingForAckLock ArrayList
             * while we're iterating through it, so we need to do it as a two-step
             * process.
             */
            ArrayList<NetworkCommand> cmdsToRemove = new ArrayList<>(3);

            @Override
            public void run()
            {
                /*
                 * We need to make sure that someone isn't trying to add to
                 * the list while we're iterating through it
                 */
                synchronized (cmdsWaitingForAckLock)
                {
                    for(NetworkCommand cmd : cmdsWaitingForAck)
                    {
                        //Are we done with this guy?
                        if(cmd.isAcknowledged())
                        {
                            cmdsToRemove.add(cmd);
                        }

                        //Nope, should we repeat him?
                        else if(cmd.shouldRepeatTx())
                        {
                            enqueueForSend(cmd);
                        }

                        //Welp, we'll have to abandon him :(
                        else
                        {
                            cmd.abandon();
                            cmdsToRemove.add(cmd);
                        }
                    }

                    cmdsWaitingForAck.removeAll(cmdsToRemove);
                    cmdsToRemove.clear();
                }
            }
        };

        repeatTxTimer = Executors.newSingleThreadScheduledExecutor();
        repeatTxTimer.scheduleAtFixedRate(repeatTxTimerRunnable, 0, 40, TimeUnit.MILLISECONDS);
    }

    @Override
    public void close()
    {
        super.close();

        repeatTxTimer.shutdown();
    }

    public void sendMsg(NetworkMsg msg)
    {
        /*
         * Is this guy a command? If so, we need to remember him
         * in order to make sure that the receiver acknowledged
         * receipt of him
         */
        if(msg instanceof NetworkCommand)
        {
            /*
             * We can't add anything to the list if the repeatTx monitor
             * thread is iterating through it
             */
            synchronized (cmdsWaitingForAckLock)
            {
                //Remember him
                cmdsWaitingForAck.add((NetworkCommand)msg);
            }
        }

        /*
         * Alright, now that those checks are taken care of, actually
         * send it off (well, add it to the txQueue anyway)
         */
        enqueueForSend(msg);
    }

    class GenericReceiver implements GenericMsgReceiver
    {
        @Override
        public void onDataReceived(NetworkMsg msg, InetAddress srcAddr)
        {
            try
            {
                msg.getMsgType();
            }catch (Exception e)
            {
                e.printStackTrace();
            }

            switch (msg.getMsgType())
            {
                case HEARTBEAT:
                {
                    callback.onHeatbeat((Heartbeat)msg, srcAddr);
                    break;
                }

                case TUNER_DATA:
                {
                    callback.onTunerData((TunerDataMsg) msg, srcAddr);
                    break;
                }

                case HUBTOOLKIT_READ_DATA:
                {
                    callback.onHubToolkitReadData((HubToolkitReadDataMsg)msg);
                    break;
                }

                case HUBTOOLKIT_WRITE_DATA:
                {
                    callback.onHubToolkitWriteData((HubToolkitWriteDataMsg)msg);
                    break;
                }

                case COMMAND:
                {
                    //Transform those bytes into a nice object - this isn't assembler!
                    NetworkCommand cmd = (NetworkCommand) msg;

                    /*
                     * Have we seen this guy before?
                     */
                    if(circularCommandCache.contains(cmd))
                    {
                        /*
                         * Sooo, it appears that we got a duplicate of a command/ack that we
                         * have in our cache. So do *not* re-trigger the callback
                         */
                        return; //Get out of dodge
                    }

                    /*
                     * Remember this guy for a while in case of duplicate tx from
                     * the other device
                     */
                    circularCommandCache.add(cmd);

                    /*
                     * If this is an acknowledgement, then that means this isn't
                     * a command to us - it's one that we issued to the other device
                     *
                     * We need to trigger the callback to notify other people that
                     * it was acknowledged, though
                     */
                    if(cmd.isAcknowledged())
                    {
                        /*
                         * We can't just call acknowledge() on the object that came in from
                         * the network, because it doesn't have any listeners attached to it.
                         * Instead, we need to find the original cmd object that was sent and
                         * call acknowledge() on that one
                         */
                        for(NetworkCommand origCmd : cmdsWaitingForAck)
                        {
                            if(origCmd.equals(cmd))
                            {
                                origCmd.acknowledge(); //Trigger the callback
                            }
                        }

                        return; //Get out of dodge
                    }

                    /*
                     * It appears this is a command to *us*
                     * We need to send an ack
                     */
                    cmd.acknowledge();
                    cmd.setDestAddr(srcAddr); //We need to send it back to the sender
                    sendMsg(cmd);

                    callback.onCommand(cmd);
                }
            }
        }
    }
}
