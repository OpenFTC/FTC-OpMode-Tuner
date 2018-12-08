/*
 * Original work Copyright (c) 2014, 2015 Qualcomm Technologies Inc
 * Modified work Copyright (c) 2018 FTC team 4634 FROGbots
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * (subject to the limitations in the disclaimer below) provided that the following conditions are
 * met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 * and the following disclaimer in the documentation and/or other materials provided with the
 * distribution.
 *
 * Neither the name of Qualcomm Technologies Inc nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS LICENSE. THIS
 * SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package net.frogbots.ftcopmodetunercommon.networking.udp;

import net.frogbots.ftcopmodetunercommon.misc.DatatypeUtil;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.util.Comparator;

/**
 * Class used to send and receive commands
 * <p>
 * These commands should be acknowledged by the receiver. The sender may resend the command
 * repeatedly until it receives and acknowledgment from the receiver. The receiver should not
 * reprocess repeated commands.
 */
public class NetworkCommand extends NetworkMsgBase implements Comparable<NetworkCommand>, Comparator<NetworkCommand>
{

    // space for the timestamp (8 bytes), ack byte (1 byte)
    private static final short cbStringLength = 2;
    private static final short cbPayloadBase = 8 + 1;

    String mName;
    String mExtra;
    long mTimestamp;
    boolean mAcknowledged = false;
    boolean abandoned = false;
    byte mAttempts = 0;
    boolean isInjected = false; // not transmitted over network
    private AckOrNackListener callback;

    /**
     * Constructor
     *
     * @param name name as string
     */
    public NetworkCommand(String name)
    {
        this(name, "");
    }

    /**
     * Constructor
     *
     * @param name  name as string
     * @param extra extra data as string
     */
    public NetworkCommand(String name, String extra)
    {
        mName = name;
        mExtra = extra;
        mTimestamp = generateTimestamp();
    }

    public NetworkCommand(byte[] byteArray)
    {
        fromByteArray(byteArray);
    }

    public void setListener(AckOrNackListener callback)
    {
        this.callback = callback;
    }

    /**
     * The receiver should call this method before sending this command back to the sender
     */
    public void acknowledge()
    {
        if(abandoned)
        {
            throw new IllegalStateException();
        }

        mAcknowledged = true;
        callback.onAck();
    }

    public void abandon()
    {
        if(mAcknowledged)
        {
            throw new IllegalStateException();
        }

        abandoned = true;
        callback.onNack();
    }

    /**
     * Check if this command has been acknowledged
     *
     * @return true if acknowledged, otherwise false
     */
    public boolean isAcknowledged()
    {
        return mAcknowledged;
    }

    /**
     * Get the command name as a string
     *
     * @return command name
     */
    public String getName()
    {
        return mName;
    }

    /**
     * Get the extra data as a string
     *
     * @return extra string
     */
    public String getExtra()
    {
        return mExtra;
    }

    /**
     * Number of times this command was packaged into a byte array
     * <p>
     * After Byte.MAX_VALUE is reached, this will stop counting and remain at Byte.MAX_VALUE.
     *
     * @return number of times this command was packaged into a byte array
     */
    public byte getAttempts()
    {
        return mAttempts;
    }

    public boolean shouldRepeatTx()
    {
        return false;
    }

    /*
     * (non-Javadoc)
     * @see com.qualcomm.robotcore.robocol.RobocolParsable#getRobocolMsgType()
     */
    @Override
    public MsgType getMsgType()
    {
        return NetworkMsg.MsgType.COMMAND;
    }

    public boolean isInjected()
    {
        return isInjected;
    }

    public void setIsInjected(boolean isInjected)
    {
        this.isInjected = isInjected;
    }

    /*
     * (non-Javadoc)
     * @see com.qualcomm.robotcore.robocol.RobocolParsable#toByteArray()
     */
    @Override
    public byte[] toByteArray()
    {

        if (mAttempts != Byte.MAX_VALUE)
        {
            mAttempts += 1;
        }

        byte[] nameBytes = DatatypeUtil.stringToByteArray(mName);
        byte[] extraBytes = DatatypeUtil.stringToByteArray(mExtra);

        short cbPayload = (short) getPayloadSize(nameBytes.length, extraBytes.length);
        if (cbPayload > Short.MAX_VALUE)
        {
            throw new IllegalArgumentException(String.format("command payload is too large: %d", cbPayload));
        }

        ByteBuffer buffer = getWriteBuffer(cbPayload);
        try
        {
            buffer.putLong(mTimestamp);
            buffer.put((byte) (mAcknowledged ? 1 : 0));
            buffer.putShort((short) nameBytes.length);
            buffer.put(nameBytes);

            // If we are just an ack, then we don't transmit the body in order to save net bandwidth
            if (!mAcknowledged)
            {
                buffer.putShort((short) extraBytes.length);
                buffer.put(extraBytes);
            }
        } catch (BufferOverflowException e)
        {
            e.printStackTrace();
        }
        return buffer.array();
    }

    int getPayloadSize(int nameBytesLength, int extraBytesLength)
    {
        if (mAcknowledged)
        {
            return cbPayloadBase + cbStringLength + nameBytesLength;
        }
        else
        {
            return cbPayloadBase + cbStringLength + nameBytesLength + cbStringLength + extraBytesLength;
        }
    }

    /*
     * (non-Javadoc)
     * @see com.qualcomm.robotcore.robocol.RobocolParsable#fromByteArray(byte[])
     */
    @Override
    public void fromByteArray(byte[] byteArray)
    {
        ByteBuffer buffer = getReadBuffer(byteArray);

        mTimestamp = buffer.getLong();
        mAcknowledged = (buffer.get() != 0);

        int cbName = DatatypeUtil.shortToUnsignedInt(buffer.getShort());
        byte[] nameBytes = new byte[cbName];
        buffer.get(nameBytes);
        mName = DatatypeUtil.byteArrayToString(nameBytes);

        if (!mAcknowledged)
        {
            int cbExtra = DatatypeUtil.shortToUnsignedInt(buffer.getShort());
            byte[] extraBytes = new byte[cbExtra];
            buffer.get(extraBytes);
            mExtra = DatatypeUtil.byteArrayToString(extraBytes);
        }
    }

    @Override
    public String toString()
    {
        return String.format("command: %20d %5s %s", mTimestamp, mAcknowledged, mName);
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof NetworkCommand)
        {
            NetworkCommand c = (NetworkCommand) o;
            if (this.mName.equals(c.mName) && this.mTimestamp == c.mTimestamp)
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public int hashCode()
    {
        return (mName.hashCode() ^ (int) mTimestamp); // xor preserves entropy
    }

    @Override
    public int compareTo(NetworkCommand another)
    {
        int diff = mName.compareTo(another.mName);

        if (diff != 0)
        {
            return diff;
        }

        if (mTimestamp < another.mTimestamp)
        {
            return -1;
        }
        if (mTimestamp > another.mTimestamp)
        {
            return 1;
        }

        return 0;
    }

    @Override
    public int compare(NetworkCommand c1, NetworkCommand c2)
    {
        return c1.compareTo(c2);
    }

    public static long generateTimestamp()
    {
        return System.nanoTime();
    }

    public interface AckOrNackListener
    {
        void onAck();
        void onNack();
    }
}