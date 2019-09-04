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

package net.frogbots.ftcopmodetuner.ui.field.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import net.frogbots.ftcopmodetuner.ui.field.FieldType;
import net.frogbots.ftcopmodetunercommon.networking.datagram.Datagram;
import net.frogbots.ftcopmodetunercommon.networking.datagram.ext.DoubleDatagram;

/**
 * This class holds the data for a DoubleFieldUi object.
 *
 * This is the object that is written to XML when saving
 * to a config file (it is then later attached to the UI
 * object when read back from the XML)
 */

@XStreamAlias("double")
public class DoubleFieldData extends FieldData
{
    @XStreamAlias("min")
    @XStreamAsAttribute
    public double min = 0;

    @XStreamAlias("max")
    @XStreamAsAttribute
    public double max = 1;

    @XStreamAlias("value")
    @XStreamAsAttribute
    public double curValue;

    public DoubleFieldData(String tag)
    {
        super(tag);
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
        dest.writeDouble(this.min);
        dest.writeDouble(this.max);
        dest.writeDouble(this.curValue);
    }

    protected DoubleFieldData(Parcel in)
    {
        super(in);
        this.min = in.readDouble();
        this.max = in.readDouble();
        this.curValue = in.readDouble();
    }

    @Override
    public Datagram toDatagram()
    {
        return new DoubleDatagram(curValue, tag);
    }

    @Override
    public FieldType getType()
    {
        return FieldType.DOUBLE;
    }

    public static final Parcelable.Creator<DoubleFieldData> CREATOR = new Parcelable.Creator<DoubleFieldData>()
    {
        @Override
        public DoubleFieldData createFromParcel(Parcel source)
        {
            return new DoubleFieldData(source);
        }

        @Override
        public DoubleFieldData[] newArray(int size)
        {
            return new DoubleFieldData[size];
        }
    };
}
