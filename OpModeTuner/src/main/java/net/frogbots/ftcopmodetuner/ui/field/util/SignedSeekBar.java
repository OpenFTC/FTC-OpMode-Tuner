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

/**
 * A wrapper around the standard Android seekbar that allows the ability
 * to set the minimum value. (Android didn't gain support for this until
 * API26, so we need to emulate it manually in order to support devices
 * below Oreo.)
 */
public class SignedSeekBar
{
    private SeekBar base;
    private int min, max = 0;

    public SignedSeekBar(SeekBar base, int min, int max)
    {
        this.base = base;
        setMinMax(min, max);
    }

    public int getProgress()
    {
        return base.getProgress() + min;
    }

    public void setProgress(int p)
    {
        base.setProgress(p-min);
    }

    public void setMinMax(int min, int max)
    {
        this.min = min;
        this.max = max;
        base.setMax(max-min);

        base.setProgress(1); // To trigger a call to the onChanged listener
        base.setProgress(0);
    }

    public void setOnSeekBarChangeListener(final OnSignedSeekBarChangeListener listener)
    {
        base.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b)
            {
                listener.onProgressChanged(SignedSeekBar.this, getProgress(), b);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });
    }

    public interface OnSignedSeekBarChangeListener
    {
        void onProgressChanged(SignedSeekBar seekBar, int progress, boolean fromUser);
    }
}