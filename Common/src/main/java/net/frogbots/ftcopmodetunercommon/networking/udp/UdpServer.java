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

/**
 * This is the server that runs on the RC to receive the incoming
 * data from the Tuner app client.
 */

public class UdpServer
{
    private int port;
    private long lastHeartbeatTime;
    private DatagramSocket serverSocket;
    private ReceiverInterface callback;
    private Thread receiverThread;
    private volatile InetAddress addrOfLastHeartbeat;

    public UdpServer(ReceiverInterface callback)
    {
        this.callback = callback;
    }

    public void openSocket(int port)
    {
        this.port = port;
        try
        {
            if (serverSocket != null)
            {
                serverSocket.close();
            }
            serverSocket = new DatagramSocket(port);
        }
        catch (SocketException e)
        {
            e.printStackTrace();
        }

        receiverThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (!Thread.currentThread().isInterrupted())
                {
                    byte[] incomingData = new byte[DataConstants.NUM_BYTES_IN_DATAGRAM_PACKET];
                    DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                    try
                    {
                        serverSocket.receive(incomingPacket);

                        byte[] bytes = new byte[incomingPacket.getLength()];
                        System.arraycopy(incomingPacket.getData(), 0, bytes, 0, incomingPacket.getLength());

                        /*
                         * This was a heartbeat packet
                         * reply to it
                         */
                        if(bytes[0] == DataConstants.HEARTBEAT_PING)
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
                            if(addrOfLastHeartbeat == null || addrOfLastHeartbeat.equals(incomingPacket.getAddress()) || !isConnected())
                            {
                                lastHeartbeatTime = System.currentTimeMillis();
                                sendBytes(new byte[]{DataConstants.HEARTBEAT_REPLY}, incomingPacket.getAddress());
                                addrOfLastHeartbeat = incomingPacket.getAddress();
                            }

                            /*
                             * Well, none of the above conditions were true, so it looks like we
                             * need to refuse this connection
                             */
                            else
                            {
                                sendBytes(new byte[]{DataConstants.CONNECTION_REFUSED}, incomingPacket.getAddress());
                            }
                        }

                        /*
                         * This was a data packet. Note that we do not really need to check the addr
                         * of the incoming data as the server won't send any data if it isn't connected,
                         * but there's really no harm in being safe (other than burning a few extra CPU cycles)
                         */
                        else
                        {
                            if(isConnected() && incomingPacket.getAddress().equals(addrOfLastHeartbeat))
                            {
                                callback.onDataReceived(bytes);
                            }
                        }

                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
        receiverThread.start();
    }

    public void sendBytes(final byte[] dataToSend, final InetAddress clientAddr)
    {
        Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {
                DatagramPacket outgoingPacket = new DatagramPacket(dataToSend, dataToSend.length, clientAddr, port);
                try
                {
                    serverSocket.send(outgoingPacket);
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        };

        new Thread(runnable).start();
    }

    public boolean isConnected()
    {
        return (System.currentTimeMillis() - lastHeartbeatTime < DataConstants.DEFAULT_CONNECTION_TIMEOUT_MS);
    }

    public void close()
    {
        receiverThread.interrupt();
        serverSocket.close();
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
        return port;
    }
}
