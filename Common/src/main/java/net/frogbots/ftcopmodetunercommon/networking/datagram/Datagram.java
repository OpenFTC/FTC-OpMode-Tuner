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

import net.frogbots.ftcopmodetunercommon.misc.DatatypeUtil;

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
    private String tagString;
    private byte[] tagBytesWithIndicator;
    private byte[] dataBytesWithIndicator;
    private byte[] dataBytes;
    private byte datatypeIndicator;

    /***
     * Constructs a new Datagram from the given parameters
     *
     * @param tagString the tag of this field/datagram
     * @param datatypeIndicator a unique byte value indicating the type of data this datagram is storing
     */
    public Datagram(String tagString, byte datatypeIndicator)
    {
        this.tagString = tagString;
        this.datatypeIndicator = datatypeIndicator;
    }

    public String getTagString()
    {
        return tagString;
    }

    /***
     * Encodes this datagram to a byte array
     *
     * @param dataBytes the datatype-specific payload bytes
     * @return the complete byte-array representation of this datagram
     */
    protected byte[] internalEncode(byte[] dataBytes)
    {
        this.dataBytes = dataBytes;
        encodeTag();
        encodeData();

        byte[] masterByteArrayWithoutIndicator = new byte[
                DATATYPE_INDICATOR_LENGTH       // Self-explanatory
              + tagBytesWithIndicator.length    // Tag bytes with 2-byte length indicator
              + dataBytesWithIndicator.length]; // Data bytes with 2-byte length indicator

        masterByteArrayWithoutIndicator[0] = datatypeIndicator;

        /*
         * Copy in the tag bytes (with the indicator)
         */
        System.arraycopy(tagBytesWithIndicator, 0, masterByteArrayWithoutIndicator, DATATYPE_INDICATOR_LENGTH, tagBytesWithIndicator.length);

        /*
         * Copy in the data bytes (with the indicator)
         */
        System.arraycopy(dataBytesWithIndicator, 0, masterByteArrayWithoutIndicator, DATATYPE_INDICATOR_LENGTH + tagBytesWithIndicator.length, dataBytesWithIndicator.length);

        byte[] masterByteArray = new byte[NUM_BYTES_IN_SHORT + masterByteArrayWithoutIndicator.length];

        /*
         * Copy in the 2 byte indicator of the overall message length to the master array
         */
        System.arraycopy(DatatypeUtil.shortToByteArray((short) masterByteArrayWithoutIndicator.length), 0, masterByteArray, 0, NUM_BYTES_IN_SHORT);

        /*
         * Copy the message data into the master array
         */
        System.arraycopy(masterByteArrayWithoutIndicator, 0, masterByteArray, NUM_BYTES_IN_SHORT, masterByteArrayWithoutIndicator.length);

        return masterByteArray;
    }

    private void encodeData()
    {
        dataBytesWithIndicator = new byte[
                  NUM_BYTES_IN_SHORT
                + dataBytes.length];

        /*
         * Copy in the 2 byte indicator of the number of data bytes
         */
        System.arraycopy(DatatypeUtil.shortToByteArray((short) dataBytes.length), 0, dataBytesWithIndicator, 0, NUM_BYTES_IN_SHORT);

        /*
         * Copy in the data bytes
         */
        System.arraycopy(dataBytes, 0, dataBytesWithIndicator, NUM_BYTES_IN_SHORT, dataBytes.length);
    }

    private void encodeTag()
    {
        tagBytesWithIndicator = new byte[
                  NUM_BYTES_IN_SHORT
                + tagString.length()];

        /*
         * Copy in the 2 byte indicator of the number of tag bytes
         */
        System.arraycopy(DatatypeUtil.shortToByteArray((short) tagString.length()), 0, tagBytesWithIndicator, 0, NUM_BYTES_IN_SHORT);

        /*
         * Copy in the tag bytes
         */
        System.arraycopy(tagString.getBytes(), 0, tagBytesWithIndicator, NUM_BYTES_IN_SHORT, tagString.getBytes().length);
    }

    /***
     * Separates out a byte array into the tag string and the actual payload bytes
     *
     * @param bytes the input byte array
     * @return a RawDatagram containing the tag string and a byte array with the actual payload bytes
     */
    protected static RawDatagram decodeToTagAndByteData(byte[] bytes)
    {
        /*
         * Figure out how many tag bytes there are
         */
        short numTagBytes = DatatypeUtil.byteArrayToShort(DatatypeUtil.getBytesByIndex(
                bytes,                           // The byte array we're reading from
                FIRST_BYTE_OF_TAG_LENGTH_POS,    // Begin reading at this byte
                SECOND_BYTE_OF_TAG_LENGTH_POS)); // Stop after reading this byte

        /*
         * Now that we know how many tag bytes there are, we can calculate
         * the position in the array of the last byte we need to read
         *
         * Note that we don't need to calculate the starting position,
         * as the amount of bytes before the tag start position is
         * always constant...
         */
        final int END_OF_TAG_BYES_POS =        // The end position is equal to:
                  BEGINNING_OF_TAG_BYTES_POS   // The begging position...
                + numTagBytes                  // Plus the number of bytes we need to read
                - 1;                           // Minus one to account for the off-by-one problem

        /*
         * Now we use the start and stop reading positions determined above
         * to actually separate out the tag bytes
         */
        byte[] tagBytes = DatatypeUtil.getBytesByIndex(
                bytes,                       // The byte array we're reading from
                BEGINNING_OF_TAG_BYTES_POS , // Begin reading at this byte
                END_OF_TAG_BYES_POS);        // Stop after reading this byte

        /*
         * Ok, since we have the bytes of the tag, all that's left to do is to
         * actually convert those bytes back into a string
         */
        String tag = DatatypeUtil.byteArrayToString(tagBytes);

        /*
         * Now that we've decoded the tag, we need to separate out the data bytes
         * First things first, we need to figure out how many data bytes we need to read
         * But, in order to do that, we need to figure out location in the array of the
         * two bytes which make up the data length indicator
         */
        final int FIRST_BYTE_OF_DATA_LENGTH_INDICATOR_POS = // The first byte position is equal to...
                BEGINNING_OF_TAG_BYTES_POS                  // The position of the first tag byte
              + numTagBytes;                                // Plus the number of tag bytes
                                                            // Note that here we don't need to add 1 because of the off-by-one problem

        /*
         * Figuring out the position of the second byte of the data length indicator
         * is trivial, since we just figured out the position of the first
         */
        final int SECOND_BYTE_OF_DATA_LENGTH_INDICATOR_POS = // All we have to do is...
                FIRST_BYTE_OF_DATA_LENGTH_INDICATOR_POS      // Take the position of the first byte
              + 1;                                           // And add 1 to it :D

        /*
         * Now that we know the positions of both of the bytes which form the data length
         * indicator, we need to read them and convert to a short
         */
        short numDataBytes = DatatypeUtil.byteArrayToShort(DatatypeUtil.getBytesByIndex(
                bytes,                                      // The array from which we're reading
                FIRST_BYTE_OF_DATA_LENGTH_INDICATOR_POS,    // Start reading at this byte
                SECOND_BYTE_OF_DATA_LENGTH_INDICATOR_POS)); // Stop after reading this byte

        /*
         * We know how many data bytes we need to read, but we also need to know
         * where they are located
         */
        final int START_OF_DATA_BYTES_POS =              // The starting byte position is equal to...
                SECOND_BYTE_OF_DATA_LENGTH_INDICATOR_POS // The position of the 2nd data length indicator byte
              + 1;                                       // Plus one (since the data bytes start right after the indicator)

        /*
         * Figuring out the end position, then, is quite simple
         */
        final int END_OF_DATA_BYTES_POS = // The ending byte position is equal to...
                START_OF_DATA_BYTES_POS   // The starting byte position
              + numDataBytes              // Plus the number of data bytes
              - 1;                        // Minus 1 to account for the off-by-one problem

        /*
         * Ok, we now know the starting and ending bytes positions of the data
         * Now we can finally read the data byte array
         */
        byte[] dataBytes = DatatypeUtil.getBytesByIndex(
                bytes,                   // The byte array we're reading from
                START_OF_DATA_BYTES_POS, // Begin reading at this byte
                END_OF_DATA_BYTES_POS);  // Stop reading at this byte

        /*
         * We have decoded the string and separated out the data bytes
         * Now we can return a RawDatagram
         */
        return new RawDatagram(tag, dataBytes);
    }

    public abstract byte[] encode();
    public abstract String getStringValue();
}
