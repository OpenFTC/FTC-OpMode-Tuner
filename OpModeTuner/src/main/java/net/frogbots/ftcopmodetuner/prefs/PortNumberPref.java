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

package net.frogbots.ftcopmodetuner.prefs;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.util.AttributeSet;

import net.frogbots.ftcopmodetuner.R;
import net.frogbots.ftcopmodetunercommon.misc.DataConstants;

/**
 * Preference for UDP port numbers
 */

public class PortNumberPref extends ValidatedEditText
{
    public PortNumberPref(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public PortNumberPref(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public PortNumberPref(Context context)
    {
        super(context);
    }

    @Override
    public String onValidate(String text)
    {
        if (!isValidPort(text))
        {
            return getContext().getString(R.string.not_valid_port);
        }
        return null;
    }

    @Override
    protected void showDialog(Bundle state)
    {
        super.showDialog(state);
        getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    private boolean isValidPort(String port)
    {
        try
        {
            int i = Integer.parseInt(port);

            if((i < DataConstants.UDP_PORT_MIN) || (i > DataConstants.UDP_PORT_MAX))
            {
                throw new IllegalArgumentException();
            }
            else
            {
                return true;
            }
        }
        catch (Exception e)
        {
            return false;
        }
    }
}
