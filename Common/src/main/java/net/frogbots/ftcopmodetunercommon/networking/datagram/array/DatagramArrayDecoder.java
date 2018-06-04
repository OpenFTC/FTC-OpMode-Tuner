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

package net.frogbots.ftcopmodetunercommon.networking.datagram.array;

import net.frogbots.ftcopmodetunercommon.misc.DataConstants;
import net.frogbots.ftcopmodetunercommon.misc.DatatypeUtil;
import net.frogbots.ftcopmodetunercommon.networking.datagram.Datagram;
import net.frogbots.ftcopmodetunercommon.networking.datagram.ext.BooleanDatagram;
import net.frogbots.ftcopmodetunercommon.networking.datagram.ext.ButtonPressDatagram;
import net.frogbots.ftcopmodetunercommon.networking.datagram.ext.ByteDatagram;
import net.frogbots.ftcopmodetunercommon.networking.datagram.ext.DoubleDatagram;
import net.frogbots.ftcopmodetunercommon.networking.datagram.ext.IntegerDatagram;
import net.frogbots.ftcopmodetunercommon.networking.datagram.ext.StringDatagram;

import java.util.ArrayList;

/**
 * Utility class for decoding Datagrams from a byte array
 */

public class DatagramArrayDecoder
{
    /***
     * Decodes a byte array into an ArrayList of Datagrams
     *
     * @param bytes the byte array to be decoded
     * @return the ArrayList containing the decoded Datagrams
     */
    public static ArrayList<Datagram> decode(byte[] bytes)
    {
        ArrayList<Datagram> datagrams = new ArrayList<>(); //The array list we'll be adding the decoded Datagrams to

        int offset = DataConstants.NUM_BYTES_IN_SHORT; //We need to account for the length of the 2-byte length indicator as well

        for (int i = 0; i < bytes.length;) //Iterate through all the bytes in the input array
        {
            /*
             * Read the 2-byte length indicator to determine the length of this Datagram
             */
            short length = DatatypeUtil.byteArrayToShort(DatatypeUtil.getBytesByIndex(bytes, i, i+1));

            /*
             * Separate out the bytes for this datagram and actually convert them into a
             * Datagram, then add them to the ArrayList
             */
            datagrams.add(byteArrayToDatagram(DatatypeUtil.getBytesByIndex(bytes, i, i + length + 1)));

            i += length; //Next time around the loop, we start reading after all the bytes we just read
            i += offset; //We need to account for the length of the 2-byte length indicator as well as it isn't included in 'length'
        }

        return datagrams; //Return the ArrayList of Datagrams we've decoded
    }

    /***
     * Decodes an individual Datagram from a byte array
     *
     * @param bytes the encoded Datagram in a byte array
     * @return the decoded Datagram
     */
    private static Datagram byteArrayToDatagram(byte[] bytes)
    {
        /*
         * Figure out what kind of datagram we're dealing with
         */
        byte typeCode = bytes[DataConstants.DATATYPE_INDICATOR_POS];

        /*
         * String
         */
        if (typeCode == DataConstants.STRING_DATATYPE_INDICATOR)
        {
            return StringDatagram.fromByteArray(bytes);
        }

        /*
         * Integer
         */
        else if(typeCode == DataConstants.INT_DATATYPE_INDICATOR)
        {
            return IntegerDatagram.fromByteArray(bytes);
        }

        /*
         * Boolean
         */
        else if(typeCode == DataConstants.BOOLEAN_DATATYPE_INDICATOR)
        {
            return BooleanDatagram.fromByteArray(bytes);
        }

        /*
         * Byte
         */
        else if(typeCode == DataConstants.BYTE_DATATYPE_INDICATOR)
        {
            return ByteDatagram.fromByteArray(bytes);
        }

        /*
         * Double
         */
        else if(typeCode == DataConstants.DOUBLE_DATATYPE_INDICATOR)
        {
            return DoubleDatagram.fromByteArray(bytes);
        }

        /*
         * Button
         */
        else if(typeCode == DataConstants.BTN_PRESS_DATATYPE_INDICATOR)
        {
            return ButtonPressDatagram.fromByteArray(bytes);
        }

        /*
         * Ruh roh!
         */
        throw new IllegalArgumentException("Byte array was not of a known type!");
    }

    /***
     * Counts the number of Datagrams in a byte array
     *
     * @param bytes the byte array containing the datagrams
     * @return the number of datagrams in the byte array
     */
    private static int countDatagramsInArray(byte[] bytes)
    {
        int count = 0; //The number of Datagrams we've found
        int offset = DataConstants.NUM_BYTES_IN_SHORT; //We need to account for the length of the 2-byte length indicator as well

        for (int i = 0; i < bytes.length;) //Iterate through all the bytes in the array
        {
            /*
             * Read the 2-byte length indicator to determine the length of this Datagram
             */
            short jumpToNext = DatatypeUtil.byteArrayToShort(DatatypeUtil.getBytesByIndex(bytes, i, i+1));

            i += jumpToNext; //Next time around the loop, we start reading after all the bytes we just read
            i += offset; //We need to account for the length of the 2-byte length indicator as well as it isn't included in 'jumpToNext'

            count ++; //Alright, that was one Datagram
        }

        return count; //Return the number of Datagrams we've found
    }
}
