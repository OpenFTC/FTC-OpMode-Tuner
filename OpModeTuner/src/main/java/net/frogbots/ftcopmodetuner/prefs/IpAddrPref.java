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
import android.util.AttributeSet;

import net.frogbots.ftcopmodetuner.R;
import net.frogbots.ftcopmodetunercommon.misc.DataConstants;

/**
 * Preference for IP addresses
 */

public class IpAddrPref extends ValidatedEditText
{
    public IpAddrPref(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public IpAddrPref(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public IpAddrPref(Context context)
    {
        super(context);
    }

    @Override
    public String onValidate(String text)
    {
        if (!isValidIp(text))
        {
            return getContext().getString(R.string.not_valid_ip_addr);
        }
        return null;
    }

    private boolean isValidIp(String ip)
    {
        try
        {
            if (ip == null || ip.isEmpty())
            {
                return false;
            }

            String[] parts = ip.split("\\.");
            if (parts.length != DataConstants.NUM_SECTIONS_IN_IP_ADDR)
            {
                return false;
            }

            for (String s : parts)
            {
                int i = Integer.parseInt(s);
                if ((i < DataConstants.BYTE_MIN_VALUE) || (i > DataConstants.BYTE_MAX_VALUE))
                {
                    return false;
                }
            }
            if (ip.endsWith("."))
            {
                return false;
            }

            return true;
        } catch (NumberFormatException nfe)
        {
            return false;
        }
    }
}
