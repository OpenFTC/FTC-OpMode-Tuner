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

package net.frogbots.ftcopmodetuner.ui.dialogs.fieldsettings;

import android.content.Context;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import net.frogbots.ftcopmodetuner.R;
import net.frogbots.ftcopmodetuner.ui.field.base.FieldInterface;
import net.frogbots.ftcopmodetuner.ui.field.base.FieldUi;

import java.util.Locale;

/**
 * Base number settings dialog from which Double and Int dialogs extend
 */

public abstract class NumberFieldSettingsDialog extends FieldSettingsDialog
{
    EditText min;
    EditText max;

    NumberFieldSettingsDialog(Context context, FieldInterface fieldInterface, FieldUi field)
    {
        super(context, fieldInterface, field);
    }

    @Override
    protected void setupUi()
    {
        super.setupUi();
        max = viewInflated.findViewById(R.id.max);
        min = viewInflated.findViewById(R.id.min);
        viewInflated.findViewById(R.id.minMaxTxtBoxes).setVisibility(View.VISIBLE);
    }

    @Override
    protected void registerTextWatcherForEditTexts(TextWatcher watcher)
    {
        super.registerTextWatcherForEditTexts(watcher);
        max.addTextChangedListener(watcher);
        min.addTextChangedListener(watcher);
    }

    @Override
    public boolean otherErrors()
    {
        boolean maxNotValidErr = !validateNumber(max.getText().toString());
        boolean minNotValidErr = !validateNumber(min.getText().toString());
        boolean minGreaterThanMaxErr = false;
        String errString = String.format(Locale.US, getContext().getString(R.string.not_a_valid_xyz), getTypeString());

        /*
         * Check if max is valid
         */
        if (maxNotValidErr)
        {
            max.setError(errString);
        }
        else
        {
            max.setError(null);
        }

        /*
         * Check if min is valid
         */
        if (minNotValidErr)
        {
            min.setError(errString);
        }

        if (!(maxNotValidErr || minNotValidErr))
        {
            if (minGreaterThanMax())
            {
                minGreaterThanMaxErr = true;
                min.setError(getContext().getString(R.string.min_greater_than_max));
            } else
            {
                minGreaterThanMaxErr = false;
                min.setError(null);
            }
        }


        return maxNotValidErr || minGreaterThanMaxErr || minNotValidErr;
    }

    protected abstract boolean minGreaterThanMax();
    protected abstract boolean validateNumber(String str);
    protected abstract String getTypeString();
}
