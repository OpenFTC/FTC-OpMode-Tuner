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

package net.frogbots.ftcopmodetunercommon.networking.receiver;

import android.content.Context;
import android.net.wifi.WifiManager;

import net.frogbots.ftcopmodetunercommon.networking.datagram.Datagram;
import net.frogbots.ftcopmodetunercommon.networking.datagram.array.DatagramArrayDecoder;
import net.frogbots.ftcopmodetunercommon.networking.datagram.ext.BooleanDatagram;
import net.frogbots.ftcopmodetunercommon.networking.datagram.ext.ButtonPressDatagram;
import net.frogbots.ftcopmodetunercommon.networking.datagram.ext.ByteDatagram;
import net.frogbots.ftcopmodetunercommon.networking.datagram.ext.DoubleDatagram;
import net.frogbots.ftcopmodetunercommon.networking.datagram.ext.IntegerDatagram;
import net.frogbots.ftcopmodetunercommon.networking.datagram.ext.StringDatagram;
import net.frogbots.ftcopmodetunercommon.networking.udp.RcUdpSocket;
import net.frogbots.ftcopmodetunercommon.networking.udp.UdpSocket;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Locale;

/**
 * This is the class you can use in your OpModes to receive the values
 * sent from the Tuner app - HOWEVER, it is recommended to simply change
 * your opmode to extend TunableLinearOpMode or TunableOpMode instead,
 * for ease of use purposes...
 */

public class FtcOpModeTunerReceiver implements UdpSocket.Receiver
{
    private RcUdpSocket server;
    private FtcOpModeTunerReceiverInterface callback;
    private WifiManager.WifiLock wifiLock;
    private ArrayList<IntegerDatagram> integerDatagrams = new ArrayList<>();
    private ArrayList<StringDatagram> stringDatagrams = new ArrayList<>();
    private ArrayList<BooleanDatagram> booleanDatagrams = new ArrayList<>();
    private ArrayList<ByteDatagram> byteDatagrams = new ArrayList<>();
    private ArrayList<DoubleDatagram> doubleDatagrams = new ArrayList<>();

    /***
     * Constructs a new FtcOpModeTunerReceiver from the given parameters and performs some initialization
     *
     * @param context the Context of the current activity, accessed from an OpMode through hardwareMap.appContext
     * @param callback a class that implements the callback interface
     */
    public FtcOpModeTunerReceiver(Context context, FtcOpModeTunerReceiverInterface callback)
    {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiLock = wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL_HIGH_PERF, "");
        server = new RcUdpSocket(this);
        this.callback = callback;
    }

    /***
     * Acquires a high-performance wifi lock and opens the UDP server
     *
     * @param port the port on which to open the UDP server
     */
    public synchronized void begin(int port)
    {
        wifiLock.acquire();
        server.open(port);
    }

    /***
     * Closes the UDP server and releases the
     * high-performance wifi lock
     */
    public synchronized void close()
    {
        server.close();
        wifiLock.release();
    }

    /***
     * Querys as to whether or not the UDP server is connected to the Tuner client
     *
     * @return a boolean indicating connection status
     */
    public synchronized boolean isConnected()
    {
        return server.isConnected();
    }

    /***
     * Clears all of the datatype-specific array lists
     */
    private synchronized void clearAllData()
    {
        booleanDatagrams.clear();
        integerDatagrams.clear();
        stringDatagrams.clear();
        byteDatagrams.clear();
        doubleDatagrams.clear();
    }

    /***
     * Call this to update all of the data ArrayLists with a newly arrived data packet
     *
     * @param data the new data packet in raw, encoded byte array form
     */
    @Override
    public synchronized void onDataReceived(byte[] data, InetAddress srcAddr)
    {
        clearAllData(); //First things first, nuke all the old data - we don't want duplicate entries!

        ArrayList<Datagram> datagrams = DatagramArrayDecoder.decode(data); //Decode that raw byte array into an ArrayList of Datagrams

        for(Datagram d : datagrams) //Iterate through all the datagrams we found in the byte array
        {
            /*
             * Figure out what type of datagram this is and add it to the
             * ArrayList of the matching type
             */

            if(d instanceof StringDatagram)
            {
                stringDatagrams.add((StringDatagram) d);
            }
            else if(d instanceof IntegerDatagram)
            {
                integerDatagrams.add((IntegerDatagram)d);
            }
            else if(d instanceof BooleanDatagram)
            {
                booleanDatagrams.add((BooleanDatagram) d);
            }
            else if(d instanceof ByteDatagram)
            {
                byteDatagrams.add((ByteDatagram)d);
            }
            else if(d instanceof DoubleDatagram)
            {
                doubleDatagrams.add((DoubleDatagram)d);
            }
            else if(d instanceof ButtonPressDatagram)
            {
                /*
                 * For buttons, we don't add anything to an ArrayList, but rather
                 * we trigger an interface callback in the user code, passing them
                 * the tag string of the event.
                 */
                onBtnPressEvent(d.getTagString());
            }
        }
    }

    /***
     * Call this to trigger a ButtonPressEvent callback in user code
     *
     * @param tag that tag of the ButtonPressEvent to be passed to the user
     */
    private synchronized void onBtnPressEvent(final String tag)
    {
        /*
         * NOTE - we run the callback in another thread because
         * umm... well... blocking the receiver thread to run
         * user OpMode code would be kinda dumb lol
         */
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                callback.onButtonPressEvent(tag);
            }
        }).start();
    }

    /***
     * Querys the IP address of the Tuner app client we're connected to
     *
     * @return a String with the IP address and port number of the Tuner app client we're connected to
     */
    public synchronized String getTunerAppAddr()
    {
        String IP = server.getTunerAppIpAddr().replace("/", "");
        int port = server.getPort();
        return String.format(Locale.US, "%s : %d", IP, port);
    }

    //------------------------------------------------------------------
    // Strings
    //------------------------------------------------------------------

    /***
     * Retrieves a single String sent by the OpMode tuner app by it's tag.
     *
     * @param tag the tag of the String to be retrieved
     * @return the String tagged by the specified tag
     */
    public synchronized String getString(String tag)
    {
        for(StringDatagram d : stringDatagrams)
        {
            if(d.getTagString().equals(tag))
            {
                return d.getDataString();
            }
        }
        return "";
    }

    /***
     * Retrieves all of the String fields sent by the OpModeTuner app
     *
     * @return an ArrayList<StringDatagram> containing all the Strings
     * sent by the OpModeTuner app
     */
    public synchronized ArrayList<StringDatagram> getAllStrings()
    {
        /*
         * NOTE: We need to create a new ArrayList because otherwise
         * we simply return a pointer to the one in this class which
         * will get updated in the background, which would cause a
         * ConcurrentModificationException if user code was iterating
         * though it at the same time it was getting updated by the
         * background thread.
         */
        return new ArrayList<>(stringDatagrams);
    }

    //------------------------------------------------------------------
    // Integers
    //------------------------------------------------------------------

    /***
     * Retrieves a single integer sent by the OpMode tuner app by it's tag.
     *
     * @param tag the tag of the int to be retrieved
     * @return the int tagged by the specified tag
     */
    public synchronized int getInt(String tag)
    {
        for(IntegerDatagram d : integerDatagrams)
        {
            if(d.getTagString().equals(tag))
            {
                return d.getValue();
            }
        }
        return 0;
    }

    /***
     * Retrieves all of the Integer fields sent by the OpModeTuner app
     *
     * @return an ArrayList<IntegerDatagram> containing all the Strings
     * sent by the OpModeTuner app
     */
    public synchronized ArrayList<IntegerDatagram> getAllInts()
    {
        /*
         * NOTE: We need to create a new ArrayList because otherwise
         * we simply return a pointer to the one in this class which
         * will get updated in the background, which would cause a
         * ConcurrentModificationException if user code was iterating
         * though it at the same time it was getting updated by the
         * background thread.
         */
        return new ArrayList<>(integerDatagrams);
    }

    //------------------------------------------------------------------
    // Doubles
    //------------------------------------------------------------------

    /***
     * Retrieves a single double sent by the OpMode tuner app by it's tag.
     *
     * @param tag the tag of the double to be retrieved
     * @return the double tagged by the specified tag
     */
    public synchronized double getDouble(String tag)
    {
        for(DoubleDatagram d : doubleDatagrams)
        {
            if(d.getTagString().equals(tag))
            {
                return d.getValue();
            }
        }
        return 0;
    }

    /***
     * Retrieves all of the Double fields sent by the OpModeTuner app
     *
     * @return an ArrayList<IntegerDatagram> containing all the Doubles
     * sent by the OpModeTuner app
     */
    public synchronized ArrayList<DoubleDatagram> getAllDoubles()
    {
        /*
         * NOTE: We need to create a new ArrayList because otherwise
         * we simply return a pointer to the one in this class which
         * will get updated in the background, which would cause a
         * ConcurrentModificationException if user code was iterating
         * though it at the same time it was getting updated by the
         * background thread.
         */
        return new ArrayList<>(doubleDatagrams);
    }

    //------------------------------------------------------------------
    // Booleans
    //------------------------------------------------------------------

    /***
     * Retrieves a single boolean sent by the OpMode tuner app by it's tag.
     *
     * @param tag the tag of the boolean to be retrieved
     * @return the boolean tagged by the specified tag
     */
    public synchronized boolean getBoolean(String tag)
    {
        for(BooleanDatagram d : booleanDatagrams)
        {
            if(d.getTagString().equals(tag))
            {
                return d.getValue();
            }
        }
        return false;
    }

    /***
     * Retrieves all of the boolean fields sent by the OpModeTuner app
     *
     * @return an ArrayList<BooleanDatagram> containing all the Strings
     * sent by the OpModeTuner app
     */
    public synchronized ArrayList<BooleanDatagram> getAllBooleans()
    {
        /*
         * NOTE: We need to create a new ArrayList because otherwise
         * we simply return a pointer to the one in this class which
         * will get updated in the background, which would cause a
         * ConcurrentModificationException if user code was iterating
         * though it at the same time it was getting updated by the
         * background thread.
         */
        return new ArrayList<>(booleanDatagrams);
    }


    //------------------------------------------------------------------
    // Bytes
    //------------------------------------------------------------------

    /***
     * Retrieves a single byte sent by the OpMode tuner app by it's tag.
     *
     * @param tag the tag of the byte to be retrieved
     * @return the byte tagged by the specified tag
     */
    public synchronized byte getByte(String tag)
    {
        for(ByteDatagram d : byteDatagrams)
        {
            if(d.getTagString().equals(tag))
            {
                return d.getValue();
            }
        }
        return 0x00;
    }

    /***
     * Retrieves all of the byte fields sent by the OpModeTuner app
     *
     * @return an ArrayList<ByteDatagram> containing all the bytes
     * sent by the OpModeTuner app
     */
    public synchronized ArrayList<ByteDatagram> getAllBytes()
    {
        /*
         * NOTE: We need to create a new ArrayList because otherwise
         * we simply return a pointer to the one in this class which
         * will get updated in the background, which would cause a
         * ConcurrentModificationException if user code was iterating
         * though it at the same time it was getting updated by the
         * background thread.
         */
        return new ArrayList<>(byteDatagrams);
    }

    //------------------------------------------------------------------
    // ALL DATAGRAMS
    //------------------------------------------------------------------

    /***
     * Retrieves all of the datagrams that were sent by the Tuner app
     *
     * @return an ArrayList<Datagram> containing all of the datagrams that were sent by the Tuner app
     */
    public synchronized ArrayList<Datagram> getAll()
    {
        ArrayList<Datagram> all = new ArrayList<>();
        all.addAll(integerDatagrams);
        all.addAll(booleanDatagrams);
        all.addAll(doubleDatagrams);
        all.addAll(byteDatagrams);
        all.addAll(stringDatagrams);

        return all;
    }
}
