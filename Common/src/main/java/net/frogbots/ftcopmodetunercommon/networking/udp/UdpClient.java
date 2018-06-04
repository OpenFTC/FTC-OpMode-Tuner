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

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * This is the client that runs on the Tuner app to send
 * data to the server running on the RC
 */

public class UdpClient
{
    private DatagramSocket datagramSocket;
    private int port;
    private InetAddress serverAddr;
    private String serverIp;
    private UdpSocketInterface callback;
    private Thread receiverThread;

    public UdpClient(int port, String serverIp, UdpSocketInterface callback)
    {
        this.port = port;
        this.serverIp = serverIp;
        this.callback = callback;
    }

    public void openSocket()
    {
        try
        {
            serverAddr = InetAddress.getByName(serverIp);
        } catch (UnknownHostException e)
        {
            e.printStackTrace();
        }

        if(datagramSocket != null)
        {
            datagramSocket.close();
        }
        try
        {
            datagramSocket = new DatagramSocket(port);
        } catch (SocketException e)
        {
            e.printStackTrace();
        }

        receiverThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                byte[] incomingData = new byte[DataConstants.NUM_BYTES_IN_DATAGRAM_PACKET];

                while (!Thread.currentThread().isInterrupted())
                {
                    DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                    try
                    {
                        datagramSocket.receive(incomingPacket);
                        callback.onPacketReceived(incomingPacket);
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
        receiverThread.start();
    }

    public void sendBytesAsync(final byte[] dataToSend)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                DatagramPacket outgoingPacket = new DatagramPacket(dataToSend, dataToSend.length, serverAddr, port);
                try
                {
                    datagramSocket.send(outgoingPacket);
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void close()
    {
        receiverThread.interrupt();
        datagramSocket.close();

        /*
         * ANDROID BUG WORKAROUND HACK:
         * -------------------------------
         * datagramSocket.close() is NOT synchronous, so we need to wait
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

    public InetAddress getServerAddr()
    {
        return serverAddr;
    }

    public boolean isOpen()
    {
        return !datagramSocket.isClosed();
    }
}