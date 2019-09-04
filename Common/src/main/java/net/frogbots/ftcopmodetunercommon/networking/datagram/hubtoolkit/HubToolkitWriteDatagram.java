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

public class HubToolkitWriteDatagram
{
    private static final int NUM_FLOATS = 10;
    private static final int NUM_INTS = 5;
    private static final int NUM_BYTES = 10;
    private static final int CB_PAYLOAD =
                    NUM_FLOATS * DataConstants.NUM_BYTES_IN_FLOAT +
                    NUM_INTS * DataConstants.NUM_BYTES_IN_INT +
                    NUM_BYTES * DataConstants.NUM_BYTES_IN_BYTE;

    public float motor0power;
    public float motor1power;
    public float motor2power;
    public float motor3power;
    public int motor0targetPosition;
    public int motor1targetPosition;
    public int motor2targetPosition;
    public int motor3targetPosition;
    public byte motor0mode;
    public byte motor1mode;
    public byte motor2mode;
    public byte motor3mode;
    public byte motor0zeroPowerBehavior;
    public byte motor1zeroPowerBehavior;
    public byte motor2zeroPowerBehavior;
    public byte motor3zeroPowerBehavior;
    public byte dioDirections;
    public byte digitalOutputs;
    public float servo0pos;
    public float servo1pos;
    public float servo2pos;
    public float servo3pos;
    public float servo4pos;
    public float servo5pos;
    public int ledRgb;

    private ByteBuffer buffer;

    public byte[] encode()
    {
        buffer = ByteBuffer.allocate(CB_PAYLOAD);

        buffer.putFloat(motor0power)
                .putFloat(motor1power)
                .putFloat(motor2power)
                .putFloat(motor3power)
                .putInt(motor0targetPosition)
                .putInt(motor1targetPosition)
                .putInt(motor2targetPosition)
                .putInt(motor3targetPosition)
                .put(motor0mode)
                .put(motor1mode)
                .put(motor2mode)
                .put(motor3mode)
                .put(motor0zeroPowerBehavior)
                .put(motor1zeroPowerBehavior)
                .put(motor2zeroPowerBehavior)
                .put(motor3zeroPowerBehavior)
                .put(dioDirections)
                .put(digitalOutputs)
                .putFloat(servo0pos)
                .putFloat(servo1pos)
                .putFloat(servo2pos)
                .putFloat(servo3pos)
                .putFloat(servo4pos)
                .putFloat(servo5pos)
                .putInt(ledRgb);

        return buffer.array();
    }

    public void fromByteArray(byte[] bytes)
    {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);

        motor0power = buffer.getFloat();
        motor1power = buffer.getFloat();
        motor2power = buffer.getFloat();
        motor3power = buffer.getFloat();
        motor0targetPosition = buffer.getInt();
        motor1targetPosition = buffer.getInt();
        motor2targetPosition = buffer.getInt();
        motor3targetPosition = buffer.getInt();
        motor0mode = buffer.get();
        motor1mode = buffer.get();
        motor2mode = buffer.get();
        motor3mode = buffer.get();
        motor0zeroPowerBehavior = buffer.get();
        motor1zeroPowerBehavior = buffer.get();
        motor2zeroPowerBehavior = buffer.get();
        motor3zeroPowerBehavior = buffer.get();
        dioDirections = buffer.get();
        digitalOutputs = buffer.get();
        servo0pos = buffer.getFloat();
        servo1pos = buffer.getFloat();
        servo2pos = buffer.getFloat();
        servo3pos = buffer.getFloat();
        servo4pos = buffer.getFloat();
        servo5pos = buffer.getFloat();
        ledRgb = buffer.getInt();
    }
}
