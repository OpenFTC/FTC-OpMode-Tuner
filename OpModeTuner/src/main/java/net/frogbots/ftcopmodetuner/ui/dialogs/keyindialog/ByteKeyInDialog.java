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
import android.text.InputType;

import net.frogbots.ftcopmodetuner.ui.field.FieldUi;
import net.frogbots.ftcopmodetunercommon.field.data.ByteFieldData;
import net.frogbots.ftcopmodetunercommon.misc.DatatypeUtil;

/**
 * Dialog shown for manually entering data to a ByteField
 */

public class ByteKeyInDialog extends ManualKeyInDialog
{
    public ByteKeyInDialog(Context context, FieldUi fieldUi)
    {
        super(context, fieldUi);
    }

    @Override
    protected void setup()
    {
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(DatatypeUtil.byteToHex(((ByteFieldData)fieldUi.data).value));
    }

    @Override
    protected void validate()
    {
        try
        {
            byte b = DatatypeUtil.hexToByte(input.getText().toString());

            input.setError(null);
            doneBtn.setEnabled(true);
        }

        catch (IllegalArgumentException e)
        {
            doneBtn.setEnabled(false);
            input.setError("Not a valid byte!");
        }
    }
}
