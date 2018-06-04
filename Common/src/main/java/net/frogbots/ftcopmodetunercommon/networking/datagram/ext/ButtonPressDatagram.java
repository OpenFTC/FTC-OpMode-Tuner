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

package net.frogbots.ftcopmodetunercommon.networking.datagram.ext;

import net.frogbots.ftcopmodetunercommon.misc.DataConstants;
import net.frogbots.ftcopmodetunercommon.misc.DatatypeUtil;
import net.frogbots.ftcopmodetunercommon.networking.datagram.Datagram;
import net.frogbots.ftcopmodetunercommon.networking.datagram.RawDatagram;

/**
 * This class generates the actual encoded byte array for
 * a button field to be sent to the RC
 */

public class ButtonPressDatagram extends Datagram
{
    /***
     * Constructs a new ButtonPressDatagram from the supplied parameters
     *
     * @param tag the tag of this field/datagram
     */
    public ButtonPressDatagram(String tag)
    {
        super(tag, DataConstants.BTN_PRESS_DATATYPE_INDICATOR);
    }

    /***
     * Encodes this ButtonPressDatagram to a byte array
     *
     * @return the byte array representation of this ButtonPressDatagram
     */
    @Override
    public byte[] encode()
    {
        /*
         * We really don't need any payload data, but *shrug*
         * we'll just send a byte because reasons
         */
        return internalEncode(DatatypeUtil.booleanToByteArray(true));
    }

    /***
     * Decodes a ButtonPressDatagram from a byte array
     *
     * @param bytes the byte array to be converted
     * @return the ButtonPressDatagram converted from the byte array
     */
    public static ButtonPressDatagram fromByteArray(byte[] bytes)
    {
        RawDatagram rawDatagram = decodeToTagAndByteData(bytes);

        return new ButtonPressDatagram(rawDatagram.string);
    }

    public String getStringValue()
    {
        return null;
    }
}
