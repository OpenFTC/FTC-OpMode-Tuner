/*
 * Copyright (c) 2019 FTC team 4634 FROGbots
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

package net.frogbots.ftcopmodetuner.ui.field.util;

import android.widget.SeekBar;

import net.frogbots.ftcopmodetunercommon.misc.DataConstants;

/**
 * A wrapper around SignedSeekBar that transformers it to use
 * floating-point values.
 */
public class FloatingPointSignedSeekBar
{
    private SignedSeekBar signedSeekBar;

    public FloatingPointSignedSeekBar(SeekBar base, double min, double max)
    {
        signedSeekBar = new SignedSeekBar(base, doubleToInt(min), doubleToInt(max));
    }

    public double getProgress()
    {
        return intToDouble(signedSeekBar.getProgress());
    }

    public void setProgress(double progress)
    {
        signedSeekBar.setProgress(doubleToInt(progress));
    }

    public void setMinMax(double min, double max)
    {
        signedSeekBar.setMinMax(doubleToInt(min), doubleToInt(max));
    }

    public void setOnSeekBarChangeListener(final OnFloatingPointSeekBarChangeListener listener)
    {
        signedSeekBar.setOnSeekBarChangeListener(new SignedSeekBar.OnSignedSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SignedSeekBar seekBar, int progress, boolean fromUser)
            {
                listener.onProgressChanged(FloatingPointSignedSeekBar.this, getProgress(), fromUser);
            }
        });
    }

    public interface OnFloatingPointSeekBarChangeListener
    {
        void onProgressChanged(FloatingPointSignedSeekBar seekBar, double progress, boolean fromUser);
    }

    private double intToDouble(int i)
    {
        return i / DataConstants.INT_TO_DOUBLE_SCALAR;
    }

    private int doubleToInt(double d)
    {
        return (int) (d * DataConstants.INT_TO_DOUBLE_SCALAR);
    }
}
