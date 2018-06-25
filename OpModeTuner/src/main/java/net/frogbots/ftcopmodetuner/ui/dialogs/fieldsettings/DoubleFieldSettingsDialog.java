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

import net.frogbots.ftcopmodetuner.ui.field.base.FieldInterface;
import net.frogbots.ftcopmodetuner.ui.field.DoubleFieldUi;
import net.frogbots.ftcopmodetuner.ui.field.base.FieldUi;
import net.frogbots.ftcopmodetunercommon.field.data.DoubleFieldData;

/**
 * Dialog shown for the Settings of a DoubleField
 */

public class DoubleFieldSettingsDialog extends NumberFieldSettingsDialog
{
    public DoubleFieldSettingsDialog(Context context, FieldInterface fieldInterface, FieldUi field)
    {
        super(context, fieldInterface, field);
    }

    @Override
    protected void setupUi()
    {
        super.setupUi();

        max.setText(String.valueOf(((DoubleFieldData) field.getData()).max));
        min.setText(String.valueOf(((DoubleFieldData) field.getData()).min));

        max.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        min.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
    }

    @Override
    protected boolean minGreaterThanMax()
    {
        return Double.parseDouble(min.getText().toString()) > Double.parseDouble(max.getText().toString());
    }

    @Override
    protected boolean validateNumber(String str)
    {
        return isValidDouble(str);
    }

    @Override
    protected String getTypeString()
    {
        return "double";
    }

    @Override
    protected void onDialogPositiveBtnPressed()
    {
        super.onDialogPositiveBtnPressed();

        ((DoubleFieldUi) field).setMinMax(Double.parseDouble(min.getText().toString()), Double.parseDouble(max.getText().toString()));
    }

    private boolean isValidDouble(String str)
    {
        try
        {
            if(Double.parseDouble(str) > Integer.MAX_VALUE)
            {
                throw new NumberFormatException();
            }
            return true;
        }

        catch (NumberFormatException e)
        {
            return false;
        }
    }
}
