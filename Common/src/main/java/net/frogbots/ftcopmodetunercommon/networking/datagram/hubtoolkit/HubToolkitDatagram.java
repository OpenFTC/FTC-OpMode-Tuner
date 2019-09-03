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

package net.frogbots.ftcopmodetunercommon.networking.datagram.hubtoolkit;

import net.frogbots.ftcopmodetunercommon.misc.DataConstants;

import java.nio.ByteBuffer;

public class HubToolkitDatagram
{
    private static final int NUM_SHORTS = 17;
    private static final int NUM_INTS = 4;
    private static final int NUM_BYTES = 2;
    private static final int CB_PAYLOAD =
                    NUM_SHORTS * DataConstants.NUM_BYTES_IN_SHORT +
                    NUM_INTS * DataConstants.NUM_BYTES_IN_INT +
                    NUM_BYTES * DataConstants.NUM_BYTES_IN_BYTE;

    public byte  digitalInputs;
    public int   motor0position_enc;
    public int   motor1position_enc;
    public int   motor2position_enc;
    public int   motor3position_enc;
    public byte  motorStatus;
    public short motor0velocity_cps;
    public short motor1velocity_cps;
    public short motor2velocity_cps;
    public short motor3velocity_cps;
    public short analog0_mV;
    public short analog1_mV;
    public short analog2_mV;
    public short analog3_mV;
    public short motor0currentDraw;
    public short motor1currentDraw;
    public short motor2currentDraw;
    public short motor3currentDraw;
    public short gpioCurrentDraw;
    public short i2cCurrentDraw;
    public short totalCurrentDraw;
    public short monitor_12v;
    public short monitor_5v;

    private ByteBuffer buffer;

    public byte[] encode()
    {
        buffer = ByteBuffer.allocate(CB_PAYLOAD);

        buffer.put(digitalInputs)
                .putInt(motor0position_enc)
                .putInt(motor1position_enc)
                .putInt(motor2position_enc)
                .putInt(motor3position_enc)
                .put(motorStatus)
                .putShort(motor0velocity_cps)
                .putShort(motor1velocity_cps)
                .putShort(motor2velocity_cps)
                .putShort(motor3velocity_cps)
                .putShort(analog0_mV)
                .putShort(analog1_mV)
                .putShort(analog2_mV)
                .putShort(analog3_mV)
                .putShort(motor0currentDraw)
                .putShort(motor1currentDraw)
                .putShort(motor2currentDraw)
                .putShort(motor3currentDraw)
                .putShort(gpioCurrentDraw)
                .putShort(i2cCurrentDraw)
                .putShort(totalCurrentDraw)
                .putShort(monitor_12v)
                .putShort(monitor_5v);

        return buffer.array();
    }

    public void fromByteArray(byte[] bytes)
    {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);

        digitalInputs = buffer.get();
        motor0position_enc = buffer.getInt();
        motor1position_enc = buffer.getInt();
        motor2position_enc = buffer.getInt();
        motor3position_enc = buffer.getInt();
        motorStatus = buffer.get();
        motor0velocity_cps = buffer.getShort();
        motor1velocity_cps = buffer.getShort();
        motor2velocity_cps = buffer.getShort();
        motor3velocity_cps = buffer.getShort();
        analog0_mV = buffer.getShort();
        analog1_mV = buffer.getShort();
        analog2_mV = buffer.getShort();
        analog3_mV = buffer.getShort();
        motor0currentDraw = buffer.getShort();
        motor1currentDraw = buffer.getShort();
        motor2currentDraw = buffer.getShort();
        motor3currentDraw = buffer.getShort();
        gpioCurrentDraw = buffer.getShort();
        i2cCurrentDraw = buffer.getShort();
        totalCurrentDraw = buffer.getShort();
        monitor_12v = buffer.getShort();
        monitor_5v = buffer.getShort();
    }
}
