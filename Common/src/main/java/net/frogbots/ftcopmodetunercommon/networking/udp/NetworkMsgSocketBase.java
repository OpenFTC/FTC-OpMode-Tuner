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

import net.frogbots.ftcopmodetunercommon.misc.DataConstants;
import net.frogbots.ftcopmodetunercommon.misc.DatatypeUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.ArrayBlockingQueue;

public class NetworkMsgSocketBase
{
    public int port;
    private DatagramSocket basicSocket;
    private GenericMsgReceiver callback;
    private Thread rxThread;
    private Thread txThread;
    private ArrayBlockingQueue<NetworkMsg> txQueue = new ArrayBlockingQueue<>(25);

    public NetworkMsgSocketBase(GenericMsgReceiver callback)
    {
        this.callback = callback;
    }

    public NetworkMsgSocketBase()
    {

    }

    public void setCallback(GenericMsgReceiver callback)
    {
        this.callback = callback;
    }

    public void open(final int port)
    {
        if(callback == null)
        {
            throw new NullPointerException();
        }
        if(basicSocket != null)
        {
            basicSocket.close();
        }
        try
        {
            this.port = port;
            basicSocket = new DatagramSocket(port);
        }
        catch (SocketException e)
        {
            e.printStackTrace();
        }

        /*
         * Receiving thread
         */
        rxThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                byte[] incomingBuffer = new byte[DataConstants.NUM_BYTES_IN_DATAGRAM_PACKET_BUF];

                while (!Thread.currentThread().isInterrupted())
                {
                    DatagramPacket incomingPacket = new DatagramPacket(incomingBuffer, incomingBuffer.length);
                    try
                    {
                        basicSocket.receive(incomingPacket);

                        byte[] data = DatatypeUtil.getNBytes(incomingPacket.getData(), incomingPacket.getLength());
                        NetworkMsg msg = null;

                        switch (NetworkMsg.MsgType.fromSerializedDatagram(data))
                        {
                            case HEARTBEAT:
                            {
                                msg = new Heartbeat(data);
                                break;
                            }

                            case TUNER_DATA:
                            {
                                msg = new TunerDataMsg(data);
                                break;
                            }

                            case HUBTOOLKIT_DATA:
                            {
                                msg = new HubToolkitDataMsg(data);
                                break;
                            }

                            case COMMAND:
                            {
                                msg = new NetworkCommand(data);
                                break;
                            }
                        }

                        callback.onDataReceived(
                                msg,
                                incomingPacket.getAddress());
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });

        /*
         * Sending thread
         */
        txThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (!Thread.currentThread().isInterrupted())
                {
                    try
                    {
                        try
                        {
                            NetworkMsg parsable = txQueue.take();
                            byte[] data = parsable.toByteArrayForTransmission();
                            DatagramPacket packet = new DatagramPacket(data, data.length, parsable.getDestAddr(), port);
                            basicSocket.send(packet);
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    catch (InterruptedException e)
                    {
                        Thread.currentThread().interrupt();
                        e.printStackTrace();
                    }
                }
            }
        });

        //Start them up!
        rxThread.start();
        txThread.start();
    }

    public void enqueueForSend(NetworkMsg parsable)
    {
        try
        {
            txQueue.put(parsable);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public void close()
    {
        if(rxThread != null)
        {
            rxThread.interrupt();
        }

        if(txThread != null)
        {
            txThread.interrupt();
        }

        if(basicSocket != null)
        {
            basicSocket.close();
        }

        /*
         * ANDROID BUG WORKAROUND HACK:
         * -------------------------------
         * It seems basicSocket.close() is NOT synchronous, so we need to wait
         * for a bit to allow it to finish. Otherwise, if begin() is called
         * really quickly after this, then we'll crash with a:
         *      java.net.BindException: Address already in use
         *
         * 50ms is a complete random guess but it seems to work so....
         */
        try
        {
            Thread.sleep(50);
        } catch (Exception e) {}
    }

    public boolean isOpen()
    {
        if(basicSocket != null)
        {
            return !basicSocket.isClosed();
        }
        else
        {
            return false;
        }
    }

    public boolean isClosed()
    {
        if(basicSocket != null)
        {
            return basicSocket.isClosed();
        }
        else
        {
            return true;
        }
    }
}