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

package net.frogbots.ftcopmodetunercommon.field.data;

import android.os.Parcel;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import net.frogbots.ftcopmodetunercommon.field.FieldType;
import net.frogbots.ftcopmodetunercommon.networking.datagram.Datagram;
import net.frogbots.ftcopmodetunercommon.networking.datagram.ext.ByteDatagram;

/**
 * This class holds the data for a ByteFieldUi object.
 *
 * This is the object that is written to XML when saving
 * to a config file (it is then later attached to the UI
 * object when read back from the XML)
 */

@XStreamAlias("byte")
public class ByteFieldData extends FieldData
{
    @XStreamAsAttribute
    @XStreamAlias("value")
    public byte value = 0x4A;

    public ByteFieldData(String tag)
    {
        super(tag);
    }

    @Override
    public Datagram toDatagram()
    {
        return new ByteDatagram(value, tag);
    }

    @Override
    public FieldType getType()
    {
        return FieldType.BYTE;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        super.writeToParcel(dest, flags);
        dest.writeByte(this.value);
    }

    protected ByteFieldData(Parcel in)
    {
        super(in);
        this.value = in.readByte();
    }

    public static final Creator<ByteFieldData> CREATOR = new Creator<ByteFieldData>()
    {
        @Override
        public ByteFieldData createFromParcel(Parcel source)
        {
            return new ByteFieldData(source);
        }

        @Override
        public ByteFieldData[] newArray(int size)
        {
            return new ByteFieldData[size];
        }
    };
}
