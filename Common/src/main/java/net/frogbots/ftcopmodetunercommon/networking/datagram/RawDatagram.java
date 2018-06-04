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

/**
 * This class is used as an intermediate step in decoding the
 * incoming byte array. Since the tag bytes are always in the
 * same position in the byte array regardless of datatype, the
 * parser can extract them with hard-coded position values. It
 * then creates an array of these objects, which have a Sting
 * to hold the tag that was just decoded as well as a byte[]
 * which holds the rest of the encoded field. After that, it
 * is handed over to a datatype-specific parser to decode the
 * actual data in that byte[].
 */

public class RawDatagram
{
    public String string;
    public byte[] data;

    public RawDatagram(String string, byte[] data)
    {
        this.string = string;
        this.data = data;
    }
}
