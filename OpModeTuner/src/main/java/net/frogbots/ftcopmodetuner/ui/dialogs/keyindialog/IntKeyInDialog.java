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

package net.frogbots.ftcopmodetuner.ui.dialogs.keyindialog;

import android.content.Context;

import net.frogbots.ftcopmodetuner.ui.field.base.FieldUi;
import net.frogbots.ftcopmodetunercommon.field.data.IntFieldData;

/**
 * Dialog shown for manually entering data to an IntField
 */

public class IntKeyInDialog extends ManualKeyInDialog
{
    private int max;
    private int min;

    public IntKeyInDialog(Context context, FieldUi fieldUi)
    {
        super(context, fieldUi);
    }

    @Override
    protected void setup()
    {
        input.setText(String.valueOf(((IntFieldData) fieldUi.getData()).curValue));
        max = ((IntFieldData) fieldUi.getData()).max;
        min = ((IntFieldData) fieldUi.getData()).min;
    }

    @Override
    protected void validate()
    {
        try
        {
            int thing = Integer.parseInt(input.getText().toString());

            if(thing < min)
            {
                doneBtn.setEnabled(false);
                input.setError("Less than minimum!");
            }
            else if(thing > max)
            {
                doneBtn.setEnabled(false);
                input.setError("Greater than maximum!");
            }
            else
            {
                input.setError(null);
                doneBtn.setEnabled(true);
            }
        }

        catch (NumberFormatException e)
        {
            doneBtn.setEnabled(false);
            input.setError("Not a valid int!");
        }
    }
}
