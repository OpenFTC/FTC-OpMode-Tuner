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

import net.frogbots.ftcopmodetunercommon.networking.datagram.ext.BooleanDatagram;
import net.frogbots.ftcopmodetunercommon.networking.datagram.ext.ByteDatagram;
import net.frogbots.ftcopmodetunercommon.networking.datagram.ext.DoubleDatagram;
import net.frogbots.ftcopmodetunercommon.networking.datagram.ext.IntegerDatagram;
import net.frogbots.ftcopmodetunercommon.networking.datagram.ext.StringDatagram;

import java.util.ArrayList;

/**
 * User-methods common to TunableOpMode and TunableLinearOpMode
 */

@SuppressWarnings("unused")
interface TunableOpModeInterface
{
    int getPort();

    /*
     * Connection status TextView
     */
    void hideTunerConnectionStatus();
    void showTunerConnectionStatus();

    /*
     * Boolean
     */
    boolean getBoolean(String tag);
    ArrayList<BooleanDatagram> getAllBooleans();

    /*
     * Int
     */
    int getInt(String tag);
    ArrayList<IntegerDatagram> getAllInts();

    /*
     * String
     */
    String getString(String tag);
    ArrayList<StringDatagram> getAllStrings();

    /*
     * Byte
     */
    byte getByte(String tag);
    ArrayList<ByteDatagram> getAllBytes();

    /*
     * Double
     */
    double getDouble(String tag);
    ArrayList<DoubleDatagram> getAllDoubles();

    /*
     * Button
     */
    void onButtonPressEvent(String tag);
}
