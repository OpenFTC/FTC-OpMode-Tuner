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

import java.net.InetAddress;

public class RcUdpSocket extends NetworkMsgSocket implements SpecificMsgReceiver
{
    private long lastHeartbeatTime;
    private SpecificMsgReceiver callback;
    private volatile InetAddress addrOfLastHeartbeat;

    public RcUdpSocket()
    {
        super();
        super.setCallback(this);
    }

    public boolean isConnected()
    {
        return (System.currentTimeMillis() - lastHeartbeatTime < DataConstants.DEFAULT_CONNECTION_TIMEOUT_MS);
    }

    public void setCallback(SpecificMsgReceiver receiver)
    {
        callback = receiver;
    }

    public String getTunerAppIpAddr()
    {
        if(addrOfLastHeartbeat != null)
        {
            return addrOfLastHeartbeat.toString();
        }
        else
        {
            return "";
        }
    }

    public int getPort()
    {
        return super.port;
    }

    @Override
    public void onCommand(NetworkCommand command)
    {
        callback.onCommand(command);
    }

    @Override
    public void onHeatbeat(Heartbeat heartbeat, InetAddress srcAddr)
    {
        /*
         * Reply with an ACK to this heartbeat packet if:
         *
         * A)   The last addr is null - this indicates we were only just setup
         *      and haven't been connected to anything just yet
         *
         * B)   The addr of this packet is equal to the last addr - this indicates
         *      that the packet is from the device we're connected to
         *
         * C)   We're not connected - this indicates out previous connection timed
         *      out and we're ready to accept a connection from a new device
         */
        if(addrOfLastHeartbeat == null || addrOfLastHeartbeat.equals(srcAddr) || !isConnected())
        {
            lastHeartbeatTime = System.currentTimeMillis();
            heartbeat.acknowledge();
            heartbeat.setDestAddr(srcAddr);
            enqueueForSend(heartbeat);
            addrOfLastHeartbeat = srcAddr;
        }

        /*
         * Well, none of the above conditions were true, so it looks like we
         * need to refuse this connection
         */
        else
        {
            //enqueueForSend(new byte[]{DataConstants.CONNECTION_REFUSED}, srcAddr);
        }
    }

    @Override
    public void onTunerData(TunerDataMsg tunerDataMsg, InetAddress srcAddr)
    {
        /*
         * This was a data packet. Note that we do not really need to check the addr
         * of the incoming data as the server won't send any data if it isn't connected,
         * but there's really no harm in being safe (other than burning a few extra CPU cycles)
         */
        if(isConnected() && srcAddr.equals(addrOfLastHeartbeat))
        {
            callback.onTunerData(tunerDataMsg, srcAddr);
        }
    }

    @Override
    public void sendMsg(NetworkMsg msg)
    {
        if(addrOfLastHeartbeat != null && isConnected())
        {
            msg.setDestAddr(addrOfLastHeartbeat);
            //enqueueForSend(msg, serverAddr, port);
            super.sendMsg(msg);
        }
        else
        {
            if(msg.getMsgType() == NetworkMsg.MsgType.COMMAND)
            {
                ((NetworkCommand)msg).abandon();
            }
        }
    }
}
