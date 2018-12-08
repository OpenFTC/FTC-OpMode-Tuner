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

import net.frogbots.ftcopmodetunercommon.misc.DatatypeUtil;

public class Heartbeat extends NetworkMsgBase
{
    private boolean acknowledged = false;
    private int PAYLOAD_SIZE = 1;

    public Heartbeat(byte[] data)
    {
        fromByteArray(data);
    }

    public Heartbeat()
    {

    }

    @Override
    public MsgType getMsgType()
    {
        return MsgType.HEARTBEAT;
    }

    @Override
    public byte[] toByteArray()
    {
        return getWriteBuffer(PAYLOAD_SIZE)
                .put(DatatypeUtil.booleanToByte(acknowledged))
                .array();
    }

    @Override
    public void fromByteArray(byte[] byteArray)
    {
        acknowledged = DatatypeUtil.byteToBoolean(
                getReadBuffer(byteArray).get()
        );
    }

    public void acknowledge()
    {
        acknowledged = true;
    }
}
