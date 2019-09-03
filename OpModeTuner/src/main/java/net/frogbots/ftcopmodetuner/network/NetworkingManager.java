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

package net.frogbots.ftcopmodetuner.network;

import android.util.Log;

import net.frogbots.ftcopmodetuner.R;
import net.frogbots.ftcopmodetuner.prefs.GlobalPrefs;
import net.frogbots.ftcopmodetunercommon.misc.DataConstants;
import net.frogbots.ftcopmodetunercommon.networking.udp.CommandHandler;
import net.frogbots.ftcopmodetunercommon.networking.udp.ConnectionStatus;
import net.frogbots.ftcopmodetunercommon.networking.udp.Heartbeat;
import net.frogbots.ftcopmodetunercommon.networking.udp.HubToolkitDataHandler;
import net.frogbots.ftcopmodetunercommon.networking.udp.HubToolkitDataMsg;
import net.frogbots.ftcopmodetunercommon.networking.udp.NetworkCommand;
import net.frogbots.ftcopmodetunercommon.networking.udp.NetworkMsg;
import net.frogbots.ftcopmodetunercommon.networking.udp.NetworkMsgSocketBase;
import net.frogbots.ftcopmodetunercommon.networking.udp.SpecificMsgReceiver;
import net.frogbots.ftcopmodetunercommon.networking.udp.TunerDataMsg;
import net.frogbots.ftcopmodetunercommon.networking.udp.TunerUdpSocket;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * This class handles are the networking things for the Tuner app.
 * It extends upon the functionality of TunerUdpSocket
 */

public class NetworkingManager implements SpecificMsgReceiver
{
    private volatile ConnectionStatus connectionStatus;
    private volatile int port;
    private volatile String ipAddr;
    private volatile TunerUdpSocket tunerUdpSocket;
    private volatile long lastAckTime;
    private volatile long lastServerResponseTime;
    private volatile ScheduledExecutorService heartbeatTimer;
    private volatile Runnable heartbeatRunnable;
    private volatile boolean running;
    private volatile int serverHeartbeatInterval;
    private volatile int noResponseFromServerTimeoutMs;
    private volatile boolean connectionRefused = false;
    private volatile long initTime;

    private static NetworkingManager singletonInstance;
    private volatile ArrayList<NetworkEventsListener> listeners = new ArrayList<>();
    private volatile ArrayList<CommandHandler> commandHandlers = new ArrayList<>();
    private volatile HubToolkitDataHandler hubToolkitDataHandler;
    private final Object handlersLock = new Object();

    public synchronized void registerListener(NetworkEventsListener listener)
    {
        listeners.add(listener);
    }

    public synchronized void removeListener(NetworkEventsListener listener)
    {
        listeners.remove(listener);
    }

    public static NetworkingManager getInstance()
    {
        if(singletonInstance == null)
        {
            singletonInstance = new NetworkingManager();
        }

        return singletonInstance;
    }

    private NetworkingManager()
    {
        ipAddr = GlobalPrefs.getInstance().getString(R.string.prefkey_ipAddr, DataConstants.DEFAULT_IP_ADDR);
        port = Integer.parseInt(GlobalPrefs.getInstance().getString(R.string.prefkey_port, String.valueOf(DataConstants.DEFAULT_PORT)));
    }

    /***
     * Call this to fire up the UDP client with the supplied parameters and begin transmitting
     * heartbeat pings and, if we're connected, data packets to the server on the RC
     *
     * @param Ip the IP address of the server running on the Robot Controller
     * @param port the port to talk to the Robot Controller on
     * @param serverHeartbeatInterval send a heartbeat ping to the server on the RC every this many MS
     * @param noResponseFromServerTimeoutMs assume that we're not connected anymore if this many MS elapses without the RC sending an ACK
     */
    synchronized void begin(String Ip, int port, int serverHeartbeatInterval, int noResponseFromServerTimeoutMs)
    {
        this.serverHeartbeatInterval = serverHeartbeatInterval;
        this.noResponseFromServerTimeoutMs = noResponseFromServerTimeoutMs;
        this.port = port;
        this.ipAddr = Ip;

        internalBegin();
    }

    synchronized void reloadIfNecessary(String Ip, int port, int serverHeartbeatInterval, int noResponseFromServerTimeoutMs)
    {
        Log.e("Connection Status", "RESTARTING UDP CLIENT");
        if(!Ip.equals(this.ipAddr)
                || port != this.port
                || serverHeartbeatInterval != this.serverHeartbeatInterval
                || noResponseFromServerTimeoutMs != this.noResponseFromServerTimeoutMs)
        {
            stop();
            lastAckTime = 0;
            begin(Ip, port, serverHeartbeatInterval, noResponseFromServerTimeoutMs);
        }
    }

    private void internalBegin()
    {
        if(!running)
        {
            initTime = System.currentTimeMillis();
            onConnectionStatusAvailable(ConnectionStatus.INITIALIZING);
            running = true;
            if(tunerUdpSocket != null && tunerUdpSocket.isOpen())
            {
                tunerUdpSocket.close();
            }
            tunerUdpSocket = new TunerUdpSocket(port, ipAddr, this);
            tunerUdpSocket.open(port);
            setupHeartbeatTask();
        }
    }

    /***
     * Query as to whether the underlying UDP client is running
     *
     * @return a boolean indicating whether the underlying UDP client is running
     */
    public synchronized boolean isRunning()
    {
        return running;
    }

    public int getPort()
    {
        return port;
    }

    public String getIpAddr()
    {
        return ipAddr;
    }

    /***
     * Call this to stop the all network I/O and timed transmission tasks
     */
    synchronized void stop()
    {
        onConnectionStatusAvailable(ConnectionStatus.SHUTTING_DOWN);

        if(running)
        {
            running = false;
            tunerUdpSocket.close();
            heartbeatTimer.shutdown();
        }
    }

    private synchronized void onConnectionStatusAvailable(ConnectionStatus status)
    {
        if(connectionStatus != status) //We only need to take action if the current status is different from the last status
        {
            Log.e("Connection Status", status.toString());
            setConnectionStatusAndNotify(status);
        }
    }

    void setConnectionStatusAndNotify(ConnectionStatus status)
    {
        connectionStatus = status;
        notifyAll(connectionStatus, System.currentTimeMillis());
    }

    private synchronized void notifyAll(final ConnectionStatus status, long time)
    {
        for(NetworkEventsListener listener : listeners) //Fire off a callback to notify people that the network comms status has changed
        {
            try
            {
                listener.onNetworkStateUpdateFromBackgroundThread(status, time);
            }

            catch (Exception e)
            {
                System.err.println("Exception while notifying network listener!");
                e.printStackTrace();
            }
        }
    }

    public synchronized ConnectionStatus getConnectionStatus()
    {
        return connectionStatus;
    }

    /***
     * This is called when the underlying UDP client receives a packet
     *
     * @param data the data received by the UDP client
     */
    /*@Override
    public void onDataReceived(byte[] data, InetAddress srcAddr)
    {
        lastServerResponseTime = System.currentTimeMillis();

        if(data[0] == DataConstants.HEARTBEAT_REPLY)
        {
            //No need to check the port, the socket will only receive things on the port that
            //it is bound to (at least AFAIK)
            if(srcAddr.equals(tunerUdpSocket.getServerAddr()))
            {
                lastAckTime = lastServerResponseTime;
                connectionRefused = false;
            }
        }
        else if(data[0] == DataConstants.CONNECTION_REFUSED)
        {
            connectionRefused = true;
        }
    }*/

    private void setupHeartbeatTask()
    {
        heartbeatRunnable = new Runnable()
        {
            @Override
            public void run()
            {
                if(!running)
                {
                    return;
                }

                tunerUdpSocket.sendMsg(new Heartbeat());
                //System.out.println("bang");

                if(connectionRefused && (System.currentTimeMillis() - lastServerResponseTime) < noResponseFromServerTimeoutMs)
                {
                    onConnectionStatusAvailable(ConnectionStatus.CONNECTION_REFUSED);
                }
                else if((System.currentTimeMillis() - lastAckTime) > noResponseFromServerTimeoutMs)
                {
                    if(connectionStatus != ConnectionStatus.INITIALIZING || (connectionStatus == ConnectionStatus.INITIALIZING && (System.currentTimeMillis() - initTime) > noResponseFromServerTimeoutMs))
                    {
                        onConnectionStatusAvailable(ConnectionStatus.NOT_CONNECTED);
                    }
                }
                else
                {
                    onConnectionStatusAvailable(ConnectionStatus.CONNECTED);
                }
            }
        };

        heartbeatTimer = Executors.newSingleThreadScheduledExecutor();
        heartbeatTimer.scheduleAtFixedRate(heartbeatRunnable, 0, serverHeartbeatInterval, TimeUnit.MILLISECONDS);
    }

    public synchronized void sendMsg(NetworkMsg msg)
    {
        /*if(running && connectionStatus == ConnectionStatus.CONNECTED)
        {
            if(msg != null)
            {
                tunerUdpSocket.sendMsg(msg);
            }
        }*/

        if(running)
        {
            tunerUdpSocket.sendMsg(msg);
        }
    }

    public void registerCommandHandler(CommandHandler handler)
    {
        synchronized (handlersLock)
        {
            commandHandlers.add(handler);
        }
    }

    public void unregisterCommandHandler(CommandHandler handler)
    {
        synchronized (handlersLock)
        {
            commandHandlers.remove(handler);
        }
    }

    @Override
    public void onCommand(NetworkCommand command)
    {
        lastServerResponseTime = System.currentTimeMillis();

        synchronized (handlersLock)
        {
            for(CommandHandler handler : commandHandlers)
            {
                if(handler.handleCommand(command) == CommandHandler.Result.HANDLED)
                {
                    break;
                }
            }
        }
    }

    @Override
    public void onHeatbeat(Heartbeat heartbeat, InetAddress srcAddr)
    {
        lastServerResponseTime = System.currentTimeMillis();

        //No need to check the port, the socket will only receive things on the port that
        //it is bound to (at least AFAIK)
        if(/*packet.getPort() == port && */ srcAddr.equals(tunerUdpSocket.getServerAddr()))
        {
            lastAckTime = lastServerResponseTime;
            connectionRefused = false;
        }
    }

    @Override
    public void onTunerData(TunerDataMsg msg, InetAddress src)
    {
        lastServerResponseTime = System.currentTimeMillis();
    }

    public void setHubToolkitDataHandler(HubToolkitDataHandler handler)
    {
        synchronized (handlersLock)
        {
            hubToolkitDataHandler = handler;
        }
    }

    @Override
    public void onHubToolkitData(HubToolkitDataMsg dataMsg)
    {
        synchronized (handlersLock)
        {
            if(hubToolkitDataHandler != null)
            {
                hubToolkitDataHandler.handleHubToolkitData(dataMsg);
            }
        }
    }
}
