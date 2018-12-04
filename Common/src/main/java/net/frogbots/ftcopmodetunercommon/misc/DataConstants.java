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

/**
 * General constants used throughout the application, although
 * most of them have to do with networking and datagrams
 */

public class DataConstants
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

    public static final int NUM_BYTES_IN_DATAGRAM_PACKET_BUF = 1024;

    public static final byte HEARTBEAT_PING               = (byte) 0xFA;
    public static final byte HEARTBEAT_REPLY              = (byte) 0xFB;
    public static final byte CONNECTION_REFUSED           = (byte) 0xFC;

    public static final int DEFAULT_CONNECTION_TIMEOUT_MS = 500;
    public static final int DEFAULT_TX_INTERVAL_MS        = 50;
    public static final int TX_INTERVAL_MAX               = 1000;
    public static final int TX_INTERVAL_MIN               = 10;
    public static final int DEFAULT_HEARTBEAT_INTERVAL_MS = 100;
    public static final int DEFAULT_PORT                  = 8081;
    public static final String DEFAULT_IP_ADDR            = "192.168.49.1";

    public static final int UDP_PORT_MIN                  = 1024;
    public static final int UDP_PORT_MAX                  = 65535;
    public static final int NUM_SECTIONS_IN_IP_ADDR       = 4;

    public static final int FIRST_BYTE_OF_TAG_LENGTH_POS  = 3;
    public static final int SECOND_BYTE_OF_TAG_LENGTH_POS = 4;
    public static final int BEGINNING_OF_TAG_BYTES_POS    = 5;
    public static final int DATATYPE_INDICATOR_POS        = 2;

    public static final int NUM_BYTES_IN_DOUBLE           = 8;
    public static final int NUM_BYTES_IN_INT              = 4;
    public static final int NUM_BYTES_IN_SHORT            = 2;
    public static final int NUM_BYTES_IN_BOOLEAN          = 1;
    public static final int NUM_BYTES_IN_BYTE             = 1;
    public static final int DATATYPE_INDICATOR_LENGTH     = 1;

    public static final int BYTE_MIN_VALUE                = 0;
    public static final int BYTE_MAX_VALUE                = 255;

    public static final byte BOOLEAN_BYTE_VALUE_TRUE      = (byte) 0xFF;
    public static final byte BOOLEAN_BYTE_VALUE_FALSE     = (byte) 0x00;

    public static final byte INT_DATATYPE_INDICATOR       = 0x01;
    public static final byte DOUBLE_DATATYPE_INDICATOR    = 0x02;
    public static final byte BYTE_DATATYPE_INDICATOR      = 0x03;
    public static final byte STRING_DATATYPE_INDICATOR    = 0x04;
    public static final byte BOOLEAN_DATATYPE_INDICATOR   = 0x05;
    public static final byte BTN_PRESS_DATATYPE_INDICATOR = 0x06;

    public static final String SPACE                      = " ";
    public static final String DOUBLE_SPACE               = "  ";
    public static final double INT_TO_DOUBLE_SCALAR       = 10000;
}
