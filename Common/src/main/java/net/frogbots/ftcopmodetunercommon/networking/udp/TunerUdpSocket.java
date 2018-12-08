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

import java.net.InetAddress;
import java.net.UnknownHostException;

public class TunerUdpSocket extends NetworkMsgSocket
{
    private InetAddress serverAddr;
    private int port;

    public TunerUdpSocket(int port, String serverIp, SpecificMsgReceiver receiver)
    {
        super();
        super.setCallback(receiver);

        this.port = port;
        try
        {
            serverAddr = InetAddress.getByName(serverIp);
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public void sendMsg(NetworkMsg msg)
    {
        msg.setDestAddr(serverAddr);
        //enqueueForSend(msg, serverAddr, port);
        super.sendMsg(msg);
    }

    public InetAddress getServerAddr()
    {
        return serverAddr;
    }
}