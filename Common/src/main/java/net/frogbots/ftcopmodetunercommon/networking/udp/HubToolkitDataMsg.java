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

package net.frogbots.ftcopmodetunercommon.networking.udp;

import net.frogbots.ftcopmodetunercommon.misc.DataConstants;

import java.nio.ByteBuffer;

public class HubToolkitDataMsg extends NetworkMsgBase
{
    private byte[] data;

    public HubToolkitDataMsg(byte[] data)
    {
        fromByteArray(data);
    }

    public HubToolkitDataMsg()
    {

    }

    @Override
    public MsgType getMsgType()
    {
        return MsgType.HUBTOOLKIT_DATA;
    }

    @Override
    public byte[] toByteArray()
    {
        return getWriteBuffer(calcPayloadSize())
                .putInt(data.length)
                .put(data)
                .array();
    }

    @Override
    public void fromByteArray(byte[] byteArray)
    {
        ByteBuffer readBuffer = getReadBuffer(byteArray);

        int cbData = readBuffer.getInt();
        data = new byte[cbData];
        readBuffer.get(data);
    }

    public byte[] getData()
    {
        return data;
    }

    public void setData(byte[] data)
    {
        this.data = data;
    }

    private int calcPayloadSize()
    {
        return data.length + DataConstants.NUM_BYTES_IN_INT;
    }
}
