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

package net.frogbots.ftcopmodetunercommon.networking.datagram;

import net.frogbots.ftcopmodetunercommon.misc.DataConstants;
import net.frogbots.ftcopmodetunercommon.misc.DatatypeUtil;

import java.nio.ByteBuffer;

import static net.frogbots.ftcopmodetunercommon.misc.DataConstants.BEGINNING_OF_TAG_BYTES_POS;
import static net.frogbots.ftcopmodetunercommon.misc.DataConstants.DATATYPE_INDICATOR_LENGTH;
import static net.frogbots.ftcopmodetunercommon.misc.DataConstants.FIRST_BYTE_OF_TAG_LENGTH_POS;
import static net.frogbots.ftcopmodetunercommon.misc.DataConstants.NUM_BYTES_IN_SHORT;
import static net.frogbots.ftcopmodetunercommon.misc.DataConstants.SECOND_BYTE_OF_TAG_LENGTH_POS;

/**
 * This class generates the actual encoded byte array for
 * a field to be sent to the RC
 */

public abstract class Datagram
{
    /*
     * Datagram format
     * -------------------
     *
     * 0     // 1st byte of overall message length indicator
     * 20    // 2nd byte of overall message length indicator
     *
     * 4     // Single byte datatype indicator
     *
     * 0     // 1st byte of overall tag length indicator
     * 4     // 2nd byte of overall tag length indicator
     *
     * . . . // Tag bytes (in this case 4)
     *
     * 0     // 1st byte of data length indicator
     * 11    // 2nd byte of data length indicator
     *
     * . . . // Data bytes (in this case 11)
     */

    private String tag;
    private DataConstants.TunerDataType dataType;

    private ByteBuffer byteBuffer;

    /***
     * Constructs a new Datagram from the given parameters
     *
     * @param tag the tag of this field/datagram
     * @param dataType a unique byte value indicating the type of data this datagram is storing
     */
    public Datagram(String tag, DataConstants.TunerDataType dataType)
    {
        this.tag = tag;
        this.dataType = dataType;
    }

    public String getTag()
    {
        return tag;
    }

    public byte[] internalEncode(byte[] dataBytes)
    {
        int CB_PAYLOAD =
                        NUM_BYTES_IN_SHORT +        // overall message length indicator
                        DATATYPE_INDICATOR_LENGTH + // datatype indicator
                        NUM_BYTES_IN_SHORT +        // tag length indicator
                        tag.getBytes().length +     // tag
                        NUM_BYTES_IN_SHORT +        // data length indicator
                        dataBytes.length;           // data bytes


        byteBuffer = ByteBuffer.allocate(CB_PAYLOAD);

        byteBuffer.putShort((short) CB_PAYLOAD);
        byteBuffer.put(dataType.asByte());
        byteBuffer.putShort((short) tag.getBytes().length);
        byteBuffer.put(tag.getBytes());
        byteBuffer.putShort((short) dataBytes.length);
        byteBuffer.put(dataBytes);

        return byteBuffer.array();
    }

    protected static RawDatagram decodeToTagAndByteData(byte[] bytes)
    {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);

        buffer.getShort(); //eat the total msg len
        buffer.get(); //eat the data type

        short tagLen = buffer.getShort();
        byte[] tagBytes = new byte[tagLen];
        buffer.get(tagBytes);
        String tag = DatatypeUtil.byteArrayToString(tagBytes);

        short dataLen = buffer.getShort();
        byte[] dataBytes = new byte[dataLen];
        buffer.get(dataBytes);

        return new RawDatagram(tag, dataBytes);
    }

    public abstract byte[] encode();
    public abstract String getStringValue();
}
