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

import java.net.InetAddress;

/**
 * Interface implemented by objects that want to be sendable via a RobocolDatagram.
 *
 * @see RobocolDatagram
 */
public interface NetworkMsg
{

    /*
     * When implementing this class, you must make sure you populate the header
     * at the beginning of your buffer.
     *
     * Robocol Packet Format
     *
     *   Byte | Field
     * -------|-------------------------
     *    00  | Message type
     * 01-02  | Payload length, in bytes
     * 03-04  | Sequence number
     *    05+ | Payload
     */

    /**
     * Message Type
     */
    enum MsgType
    {
        /*
         * NOTE: when adding new message types, do not change existing message
         * type values or you will break backwards capability.
         */
        HEARTBEAT(0),
        TUNER_DATA(1),
        COMMAND(2),
        HUBTOOLKIT_DATA(3);

        private static final MsgType[] VALUES_CACHE = MsgType.values();
        private final int type;

        /**
         * Create a MsgType from a byte
         *
         * @param b
         * @return MsgType
         */
        public static MsgType fromByte(byte b)
        {
            MsgType t = null;
            try
            {
                t = VALUES_CACHE[b];
            } catch (ArrayIndexOutOfBoundsException e)
            {
                System.out.println(String.format("Cannot convert %d to MsgType: %s", b, e.toString()));
            }
            return t;
        }

        public static MsgType fromSerializedDatagram(byte[] datagram)
        {
            return fromByte(datagram[0]);
        }

        private MsgType(int type)
        {
            this.type = type;
        }

        /**
         * Return this message type as a byte
         *
         * @return message type as byte
         */
        public byte asByte()
        {
            return (byte) (type);
        }
    }

    // A header consists of the 'message type' (1 byte), 'payload size' (2 bytes), and a sequence number (2 bytes)
    int HEADER_LENGTH = 1 + 2 + 2;

    /**
     * Get the MsgType of this NetworkMsg
     *
     * @return message type
     */
    MsgType getMsgType();

    InetAddress getDestAddr();
    void setDestAddr(InetAddress inetAddress);

    /**
     * Returns the sequence number of this packet. Newly-created packets are numbered in a monotonically
     * increasing fashion, independently, on both driver station and robot controller; there are thus
     * two numbering spaces. Note that though the value here reports as an int, only two bytes are used
     * to transmit the sequence number. Reported values will thus be in the range of 0..65535.
     *
     * @return the sequence number of this packet
     */
    int getSequenceNumber();

    /**
     * Sets/updates the sequence number of the parsable to be the next available value
     */
    void setSequenceNumber();

    /**
     * Serializes the object for the purposes of network transmission, which is assumed will take
     * place virtually immediately. Internal state regarding the time of last transmission may thus
     * be updated during this method.
     *
     * @return a serialized copy of of the object
     */
    byte[] toByteArrayForTransmission();

    /**
     * Serializes the object for the purposes other than network transmission, such as creating
     * a local copy by a subsequent invocation of fromByteArray() into another instance.
     *
     * @return a serialized copy of the object
     */
    byte[] toByteArray();

    /**
     * Populate the fields of this object based on values of this byte array.
     *
     * @param byteArray byte array from which to populate this object
     */
    void fromByteArray(byte[] byteArray);
}
