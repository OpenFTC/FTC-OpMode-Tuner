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
import android.text.InputType;

import net.frogbots.ftcopmodetuner.ui.field.FieldInterface;
import net.frogbots.ftcopmodetuner.ui.field.FieldUi;
import net.frogbots.ftcopmodetuner.ui.field.IntFieldUi;
import net.frogbots.ftcopmodetunercommon.field.data.IntFieldData;

/**
 * Dialog shown for the Settings of an IntField
 */

public class IntFieldSettingsDialog extends NumberFieldSettingsDialog
{
    public IntFieldSettingsDialog(Context context, FieldInterface fieldInterface, FieldUi field)
    {
        super(context, fieldInterface, field);
    }

    @Override
    protected void setupUi()
    {
        super.setupUi();

        max.setText(String.valueOf(((IntFieldData) field.data).max));
        min.setText(String.valueOf(((IntFieldData) field.data).min));

        max.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
        min.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
    }

    @Override
    protected boolean minGreaterThanMax()
    {
        return Integer.parseInt(min.getText().toString()) > Integer.parseInt(max.getText().toString());
    }

    @Override
    protected boolean validateNumber(String str)
    {
        return isValidInt(str);
    }

    @Override
    protected String getTypeString()
    {
        return "integer";
    }

    @Override
    protected void onDialogPositiveBtnPressed()
    {
        super.onDialogPositiveBtnPressed();

        ((IntFieldUi) field).setMinMax(Integer.parseInt(min.getText().toString()), Integer.parseInt(max.getText().toString()));
    }

    private boolean isValidInt(String str)
    {
        try
        {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e)
        {
            return false;
        }
    }
}
