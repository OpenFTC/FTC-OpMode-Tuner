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

package net.frogbots.ftcopmodetunercommon.misc;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * General utilities for converting between data types
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class DatatypeUtil
{
    //------------------------------------------------------------------
    // Integers
    //------------------------------------------------------------------

    /***
     * Converts an integer to a byte array
     *
     * @param value the int to be converted
     * @return the byte array containing the bytes of the integer
     */
    public static byte[] intToByteArray(int value)
    {
        ByteBuffer buffer = ByteBuffer.allocate(DataConstants.NUM_BYTES_IN_INT);
        buffer.putInt(value);
        return buffer.array();
    }

    /***
     * Converts a byte array to an integer
     *
     * @param bytes a 4-byte array to be converted
     * @return the int created from the 4 bytes
     */
    public static int byteArrayToInt(byte[] bytes)
    {
        if(bytes.length != DataConstants.NUM_BYTES_IN_INT)
        {
            throw new IllegalArgumentException("An int cannot be " + bytes.length + " bytes!");
        }

        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return buffer.getInt();
    }

    //------------------------------------------------------------------
    // Shorts
    //------------------------------------------------------------------

    /***
     * Converts a short to a byte array
     *
     * @param value the short to be converted
     * @return the byte array containing the bytes of the short
     */
    public static byte[] shortToByteArray(short value)
    {
        ByteBuffer buffer = ByteBuffer.allocate(DataConstants.NUM_BYTES_IN_SHORT);
        buffer.putShort(value);
        return buffer.array();
    }

    //------------------------------------------------------------------
    // Unsigned --> integer
    //------------------------------------------------------------------

    /***
     * Returns (as an int) the value of a byte as if it were unsigned
     *
     * @param b the byte to return the unsigned representation of
     * @return an int that is the unsigned representation of the byte
     */
    public static int byteToUnsignedInt(byte b)
    {
        //Needs >= JDK8
        //return Byte.toUnsignedInt(b);

        return ((int)(b) & 0xff);
    }

    /***
     * Returns (as an int) the value of a short as if it were unsigned
     *
     * @param s the short to return the unsigned representation of
     * @return an int that is the unsigned representation of the short
     */
    public static int shortToUnsignedInt(short s)
    {
        //Needs >= JDK8
        //return Short.toUnsignedInt(s);

        return ((int)(s) & 0xffff);
    }

    /***
     * Converts a byte array to a short
     *
     * @param bytes a 2-byte array to be converted
     * @return the short created from the 2 bytes
     */
    public static short byteArrayToShort(byte[] bytes)
    {
        if(bytes.length != DataConstants.NUM_BYTES_IN_SHORT)
        {
            throw new IllegalArgumentException("A short cannot be " + bytes.length + " bytes!");
        }

        ByteBuffer buffer = ByteBuffer.wrap(bytes);

        short debugIntermediateStep = buffer.getShort();

        return debugIntermediateStep;
    }

    //------------------------------------------------------------------
    // Doubles
    //------------------------------------------------------------------

    /***
     * Converts a double to a byte array
     *
     * @param value the double to be converted
     * @return the byte array containing the bytes of the double
     */
    public static byte[] doubleToByteArray(double value)
    {
        ByteBuffer buffer = ByteBuffer.allocate(DataConstants.NUM_BYTES_IN_DOUBLE);
        buffer.putDouble(value);
        return buffer.array();
    }

    /***
     * Converts a byte array to a double
     *
     * @param bytes the 8-byte array to be converted
     * @return the double created from the 8 bytes
     */
    public static double byteArrayToDouble(byte[] bytes)
    {
        if(bytes.length != DataConstants.NUM_BYTES_IN_DOUBLE)
        {
            throw new IllegalArgumentException("A double cannot be " + bytes.length + " bytes!");
        }

        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return buffer.getDouble();
    }

    //------------------------------------------------------------------
    // Strings
    //------------------------------------------------------------------

    /***
     * Converts a byte array to a String
     *
     * @param bytes a byte array containing UTF-8 char bytes
     * @return the String created from the char bytes
     */
    public static String byteArrayToString(byte[] bytes)
    {
        try
        {
            return new String(bytes, "UTF-8");
        }

        catch (UnsupportedEncodingException e)
        {
            throw new IllegalArgumentException("Bytes were not a UTF-8 string!");
        }
    }

    /***
     * Converts a string to a byte array
     *
     * @param string the string to be converted to a byte[]
     * @return a byte[] with the UTF-8 representation of the String
     */
    public static byte[] stringToByteArray(String string)
    {
        try
        {
            return string.getBytes("UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            throw new IllegalArgumentException("Failed to convert string to UTF-8!");
        }
    }

    //------------------------------------------------------------------
    // Booleans
    //------------------------------------------------------------------

    /***
     * Converts a boolean to a byte
     *
     * @param value the boolean to convert
     * @return the byte value of the boolean
     */
    public static byte booleanToByte(boolean value)
    {
        if(value)
        {
            return DataConstants.BOOLEAN_BYTE_VALUE_TRUE;
        }
        else
        {
            return DataConstants.BOOLEAN_BYTE_VALUE_FALSE;
        }
    }

    /***
     * Converts a byte to a boolean
     *
     * @param value the byte to convert
     * @return the boolean value of the byte
     */
    public static boolean byteToBoolean(byte value)
    {
        if(value == DataConstants.BOOLEAN_BYTE_VALUE_TRUE)
        {
            return true;
        }
        else if(value == DataConstants.BOOLEAN_BYTE_VALUE_FALSE)
        {
            return false;
        }
        else
        {
            throw new IllegalArgumentException("A boolean byte cannot be " + value + ", it must either be 0x00 or 0x01!");
        }
    }

    /***
     * Converts a boolean to a byte array
     *
     * @param value the boolean to be converted
     * @return a single-byte array that will be either
     * 0x00 for false or 0xFF for true
     */
    public static byte[] booleanToByteArray(boolean value)
    {
        byte[] array = new byte[DataConstants.NUM_BYTES_IN_BOOLEAN];

        if(value)
        {
            array[0] = DataConstants.BOOLEAN_BYTE_VALUE_TRUE;
        }
        else
        {
            array[0] = DataConstants.BOOLEAN_BYTE_VALUE_FALSE;
        }

        return array;
    }

    /***
     * Converts an array of bytes into a boolean
     * The array must only be one byte long, and
     * that one byte must be either 0x00 or 0xFF
     *
     * @param bytes the one-byte array
     * @return The boolean
     */
    public static boolean byteArrayToBoolean(byte[] bytes)
    {
        if(bytes.length != DataConstants.NUM_BYTES_IN_BOOLEAN)
        {
            throw new IllegalArgumentException("A boolean cannot be " + bytes.length + " bytes!");
        }

        if(bytes[0] == DataConstants.BOOLEAN_BYTE_VALUE_TRUE)
        {
            return true;
        }
        else if(bytes[0] == DataConstants.BOOLEAN_BYTE_VALUE_FALSE)
        {
            return false;
        }
        else
        {
            throw new IllegalArgumentException("A boolean byte cannot be " + bytes[0] + ", it must either be 0x00 or 0x01!");
        }
    }

    //------------------------------------------------------------------
    // Bytes
    //------------------------------------------------------------------

    /***
     * Converts a byte into a single-element byte array
     *
     * @param value the byte to be converted
     * @return the array containing the 'value' byte
     */
    public static byte[] byteToByteArray(byte value)
    {
        return new byte[]{value};
    }

    /***
     * Converts a single-element byte array into a standalone byte
     *
     * @param bytes the single-element byte array
     * @return the byte from the single-element array
     */
    public static byte byteArrayToByte(byte[] bytes)
    {
        if(bytes.length != DataConstants.NUM_BYTES_IN_BYTE)
        {
            throw new IllegalArgumentException("A byte cannot be " + bytes.length + " bytes!");
        }

        return bytes[0];
    }

    //------------------------------------------------------------------
    // Byte arrays
    //------------------------------------------------------------------

    /***
     * Retrieves a certain chunk of bytes from an array
     *
     * @param bytes the byte array to pull from
     * @param start the index of the byte in the array that will be the first byte in the chunk
     * @param end the index of the byte in the array that will be the last byte in the chunk
     * @return the chunk of selected bytes
     */
    public static byte[] getBytesByIndex(byte[] bytes, int start, int end)
    {
        byte[] things = new byte[end - start + 1];

        System.arraycopy(bytes, start, things, 0, things.length);

        return things;
    }

    /***
     * Retrieves a certain chunk of bytes from an array
     *
     * @param bytes the byte array to pull from
     * @param start the index of the byte in the array that will be the first byte in the chunk
     * @param n the number of bytes to pull, beginning from the 'start' position
     * @return the chunk of selected bytes
     */
    public static byte[] getNBytes(byte[] bytes, int start, int n)
    {
        byte[] things = new byte[n];

        System.arraycopy(bytes, start, things, 0, n);

        return things;
    }

    /***
     * Retrieves a certain chunk of bytes from the front
     * of an array
     *
     * @param bytes the byte array to pull from
     * @param n the number of bytes to pull starting from the front
     * @return the chunk of selected bytes
     */
    public static byte[] getNBytes(byte[] bytes, int n)
    {
        return getNBytes(bytes, 0, n);
    }

    //------------------------------------------------------------------
    // Hexadecimal
    //------------------------------------------------------------------

    /***
     * Converts a hexadecimal String into a byte
     *
     * @param hex the hex String to be converted
     *            must be in the form of 0xA7
     * @return the byte converted from the hex String
     */
    public static byte hexToByte(String hex)
    {
        if(!hex.startsWith("0x"))
        {
            throw new IllegalArgumentException();
        }

        hex = hex.replace("0x", "");

        if(hex.length() != 2)
        {
            throw new IllegalArgumentException();
        }

        return parseHexBinary(hex)[0];
    }

    /***
     * Converts a single byte into a hexadecimal String
     *
     * @param theByte the byte to be converted
     * @return the hex string converted from the byte
     * will be in the form of 0xA7
     */
    public static String byteToHex(byte theByte)
    {
        return "0x" + printHexBinary(new byte[]{theByte});
    }

    //---------------------------------------------------------------------------------------------------------------
    // Methods from DatatypeConverter.java of the JDK (Android doesn't have it)
    // See that file for the license covering these methods
    //---------------------------------------------------------------------------------------------------------------

    private static final char[] hexCode = "0123456789ABCDEF".toCharArray();

    private static String printHexBinary(byte[] data)
    {
        StringBuilder r = new StringBuilder(data.length * 2);

        for (byte b : data)
        {
            r.append(hexCode[(b >> 4) & 0xF]);
            r.append(hexCode[(b & 0xF)]);
        }

        return r.toString();
    }

    private static byte[] parseHexBinary(String s)
    {
        final int len = s.length();

        // "111" is not a valid hex encoding.
        if (len % 2 != 0)
        {
            throw new IllegalArgumentException("hexBinary needs to be even-length: " + s);
        }

        byte[] out = new byte[len / 2];

        for (int i = 0; i < len; i += 2)
        {
            int h = hexToBin(s.charAt(i));
            int l = hexToBin(s.charAt(i + 1));

            if (h == -1 || l == -1)
            {
                throw new IllegalArgumentException("contains illegal character for hexBinary: " + s);
            }

            out[i / 2] = (byte) (h * 16 + l);
        }

        return out;
    }

    private static int hexToBin(char ch)
    {
        if ('0' <= ch && ch <= '9')
        {
            return ch - '0';
        }

        if ('A' <= ch && ch <= 'F')
        {
            return ch - 'A' + 10;
        }

        if ('a' <= ch && ch <= 'f')
        {
            return ch - 'a' + 10;
        }

        return -1;
    }
}
