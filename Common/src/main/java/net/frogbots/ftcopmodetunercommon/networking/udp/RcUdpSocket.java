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

public class RcUdpSocket extends UdpSocket implements UdpSocket.Receiver
{
    private long lastHeartbeatTime;
    private UdpSocket.Receiver callback;
    private volatile InetAddress addrOfLastHeartbeat;

    public RcUdpSocket(UdpSocket.Receiver callback)
    {
        super();
        super.setReceiver(this);
        this.callback = callback;
    }

    @Override
    public void onDataReceived(byte[] data, InetAddress srcAddr)
    {
        /*
         * This was a heartbeat packet
         * reply to it
         */
        if(data[0] == DataConstants.HEARTBEAT_PING)
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
                enqueueForSend(new byte[]{DataConstants.HEARTBEAT_REPLY}, srcAddr);
                addrOfLastHeartbeat = srcAddr;
            }

            /*
             * Well, none of the above conditions were true, so it looks like we
             * need to refuse this connection
             */
            else
            {
                enqueueForSend(new byte[]{DataConstants.CONNECTION_REFUSED}, srcAddr);
            }
        }

        /*
         * This was a data packet. Note that we do not really need to check the addr
         * of the incoming data as the server won't send any data if it isn't connected,
         * but there's really no harm in being safe (other than burning a few extra CPU cycles)
         */
        else
        {
            if(isConnected() && srcAddr.equals(addrOfLastHeartbeat))
            {
                callback.onDataReceived(data, srcAddr);
            }
        }
    }

    public boolean isConnected()
    {
        return (System.currentTimeMillis() - lastHeartbeatTime < DataConstants.DEFAULT_CONNECTION_TIMEOUT_MS);
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
}
