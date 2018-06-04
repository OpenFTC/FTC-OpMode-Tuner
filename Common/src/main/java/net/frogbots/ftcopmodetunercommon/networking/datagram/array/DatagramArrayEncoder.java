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

import net.frogbots.ftcopmodetunercommon.networking.datagram.Datagram;

import java.util.ArrayList;

/**
 * Utility class for encoding Datagrams into a byte array
 */

public class DatagramArrayEncoder
{
    /***
     * Encodes an ArrayList of Datagrams into a byte array
     *
     * @param datagrams the ArrayList of Datagrams to be converted
     * @return the byte array of encoded datagrams
     */
    public static byte[] encode(ArrayList<Datagram> datagrams)
    {
        /*
         * The byte array into which we'll be putting the encoded datagrams
         */
        byte[] masterByteArray = new byte[getTotalByteArraySizeNeeded(datagrams)];

        int i = 0; //Int to keep track of our position in the masterByteArray

        for(Datagram d : datagrams) //Iterate through all the datagrams in the ArrayList
        {
            byte[] datagramBytes = d.encode(); //Encode each one

            /*
             * Chuck all the bytes from that datagram into the masterByteArray at the
             * at the position indicated by 'i'
             */
            System.arraycopy(datagramBytes,0, masterByteArray, i, datagramBytes.length);

            i+= datagramBytes.length; //Update our position in the masterByteArray
        }

        return masterByteArray;
    }

    /***
     * Computes the number of bytes needed to store the entire ArrayList
     * of Datagrams into a byte array
     *
     * @param datagrams the ArrayList of Datagrams
     * @return the number of bytes required to store the entire ArrayList
     * of Datagrams into a byte array
     */
    private static int getTotalByteArraySizeNeeded(ArrayList<Datagram> datagrams)
    {
        int i = 0; //Int to keep track of how many bytes we need

        for(Datagram d : datagrams) //Iterate through the all the Datagrams in the ArrayList
        {
            i += d.encode().length; //Add the amount of bytes needed for each datagram
        }

        return i; //Return the number of bytes needed
    }
}
