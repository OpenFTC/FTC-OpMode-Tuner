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
 * a byte field to be sent to the RC
 */

public class ByteDatagram extends Datagram
{
    private byte value;

    /***
     * Constructs a new ByteDatagram from the supplied parameters
     *
     * @param value the byte payload value
     * @param tag the tag of this field/datagram
     */
    public ByteDatagram(byte value, String tag)
    {
        super(tag, DataConstants.TunerDataType.BYTE);
        this.value = value;
    }

    /***
     * Encodes this ByteDatagram to a byte array
     *
     * @return the byte array representation of this ByteDatagram
     */
    @Override
    public byte[] encode()
    {
        return internalEncode(DatatypeUtil.byteToByteArray(value));
    }

    /***
     * Decodes a ByteDatagram from a byte array
     *
     * @param bytes the byte array to be converted
     * @return the ByteDatagram converted from the byte array
     */
    public static ByteDatagram fromByteArray(byte[] bytes)
    {
        RawDatagram rawDatagram = decodeToTagAndByteData(bytes);

        return new ByteDatagram(DatatypeUtil.byteArrayToByte(rawDatagram.data), rawDatagram.string);
    }

    /***
     * Gets the byte payload value of this datagram
     *
     * @return the byte payload value of this datagram
     */
    public byte getValue()
    {
        return value;
    }


    /***
     * Gets the byte payload value of the datagram in String form
     *
     * @return the byte payload value of the datagram in String form
     */
    public String getStringValue()
    {
        return DatatypeUtil.byteToHex(value);
    }
}
