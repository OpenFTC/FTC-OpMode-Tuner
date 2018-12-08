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

package net.frogbots.ftcopmodetuner.robotcontrollerlib;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import net.frogbots.ftcopmodetunercommon.networking.datagram.ext.BooleanDatagram;
import net.frogbots.ftcopmodetunercommon.networking.datagram.ext.ByteDatagram;
import net.frogbots.ftcopmodetunercommon.networking.datagram.ext.DoubleDatagram;
import net.frogbots.ftcopmodetunercommon.networking.datagram.ext.IntegerDatagram;
import net.frogbots.ftcopmodetunercommon.networking.datagram.ext.StringDatagram;

import java.util.ArrayList;

/**
 * Make your OpMode extend this in order to be able to retrieve values sent
 * by in the Tuner app in your code
 */
@SuppressWarnings("unused")
public abstract class TunableLinearOpMode extends LinearOpMode implements TunableOpModeInterface
{
    private TunableOpModeCommon tunableOpModeCommon;

    public TunableLinearOpMode()
    {
        tunableOpModeCommon = new TunableOpModeCommon(this);
    }

    /***
     * Override this to set a custom port for the UDP server to listen on.
     * NOTE - you will need to change the port in the Advanced Network settings of
     * the Tuner app as well, or the server and client will not be able to connect.
     *
     * @return the port for the UDP server to listen on
     */
    @Override
    public int getPort()
    {
        return tunableOpModeCommon.getPort();
    }

    //------------------------------------------------------------------
    // CONNECTION STATUS
    //------------------------------------------------------------------

    /**
     * Call this to enable using runtime UI hooking to show the connection status TextView
     * on the main screen of the Robot Controller
     */
    @Override
    public void showTunerConnectionStatus()
    {
        tunableOpModeCommon.showTunerConnectionStatus();
    }

    /**
     * Call this to disable using runtime UI hooking to show the connection status TextView
     * on the main screen of the Robot Controller. The only reason I can think of for wanting
     * to disable it would be in the case of a picky inspector
     */
    @Override
    public void hideTunerConnectionStatus()
    {
        tunableOpModeCommon.hideTunerConnectionStatus();
    }

    //------------------------------------------------------------------
    // BOOLEANS
    //------------------------------------------------------------------

    /***
     * Call this to get a boolean sent from the Tuner app
     *
     * @param tag the name that this boolean is tagged with in the Tuner app
     * @return the boolean sent from the RC with a tag matching that passed as the method parameter
     */
    @Override
    public boolean getBoolean(String tag)
    {
        return tunableOpModeCommon.receiver.getBoolean(tag);
    }

    /***
     * Call this to retrieve all the booleans sent by the Tuner app
     *
     * @return an ArrayList of all the BooleanDatagrams that were sent
     * by the tuner app in the last network datagram
     */
    @Override
    public ArrayList<BooleanDatagram> getAllBooleans()
    {
        return tunableOpModeCommon.receiver.getAllBooleans();
    }

    //------------------------------------------------------------------
    // INTEGERS
    //------------------------------------------------------------------

    /***
     * Call this to get an integer sent from the Tuner app
     *
     * @param tag the name that this integer is tagged with in the Tuner app
     * @return the integer sent from the RC with a tag matching that passed as the method parameter
     */
    @Override
    public int getInt(String tag)
    {
        return tunableOpModeCommon.receiver.getInt(tag);
    }

    /***
     * Call this to retrieve all the integers sent by the Tuner app
     *
     * @return an ArrayList of all the IntegerDatagrams that were sent
     * by the tuner app in the last network datagram
     */
    @Override
    public ArrayList<IntegerDatagram> getAllInts()
    {
        return tunableOpModeCommon.receiver.getAllInts();
    }

    //------------------------------------------------------------------
    // STRINGS
    //------------------------------------------------------------------

    /***
     * Call this to get a string sent from the Tuner app
     *
     * @param tag the name that this string is tagged with in the Tuner app
     * @return the string sent from the RC with a tag matching that passed as the method parameter
     */
    @Override
    public String getString(String tag)
    {
        return tunableOpModeCommon.receiver.getString(tag);
    }

    /***
     * Call this to retrieve all the strings sent by the Tuner app
     *
     * @return an ArrayList of all the StringsDatagrams that were sent
     * by the tuner app in the last network datagram
     */
    @Override
    public ArrayList<StringDatagram> getAllStrings()
    {
        return tunableOpModeCommon.receiver.getAllStrings();
    }

    //------------------------------------------------------------------
    // BYTES
    //------------------------------------------------------------------

    /***
     * Call this to get a byte sent from the Tuner app
     *
     * @param tag the name that this byte is tagged with in the Tuner app
     * @return the byte sent from the RC with a tag matching that passed as the method parameter
     */
    @Override
    public byte getByte(String tag)
    {
        return tunableOpModeCommon.receiver.getByte(tag);
    }

    /***
     * Call this to retrieve all the bytes sent by the Tuner app
     *
     * @return an ArrayList of all the ByteDatagrams that were sent
     * by the tuner app in the last network datagram
     */
    @Override
    public ArrayList<ByteDatagram> getAllBytes()
    {
        return tunableOpModeCommon.receiver.getAllBytes();
    }

    //------------------------------------------------------------------
    // DOUBLES
    //------------------------------------------------------------------

    /***
     * Call this to get a double sent from the Tuner app
     *
     * @param tag the name that this double is tagged with in the Tuner app
     * @return the double sent from the RC with a tag matching that passed as the method parameter
     */
    @Override
    public double getDouble(String tag)
    {
        return tunableOpModeCommon.receiver.getDouble(tag);
    }

    /***
     * Call this to retrieve all the doubles sent by the Tuner app
     *
     * @return an ArrayList of all the DoubleDatagrams that were sent
     * by the tuner app in the last network datagram
     */
    @Override
    public ArrayList<DoubleDatagram> getAllDoubles()
    {
        return tunableOpModeCommon.receiver.getAllDoubles();
    }

    //------------------------------------------------------------------
    // BUTTONS
    //------------------------------------------------------------------

    /***
     * Override this method to be able to handle button press events in
     * your OpMode. NOTE: this will be called from another thread, so
     * make sure that your OpMode is thread safe!!!
     *
     * @param tag the tag ID of the button that was pressed
     */
    @Override
    public void onButtonPressEvent(String tag){}
}
