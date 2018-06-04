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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;

/**
 * EdiText that provides a method for you to validate the input
 */

public abstract class ValidatedEditText extends CustomEditTextPref
{
    public ValidatedEditText(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public ValidatedEditText(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public ValidatedEditText(Context context)
    {
        super(context);
    }

    @Override
    protected void showDialog(Bundle state)
    {
        super.showDialog(state);
        AlertDialog dlg = (AlertDialog) getDialog();
        final View positiveButton = dlg.getButton(DialogInterface.BUTTON_POSITIVE);

        onValidate(getEditText().getText().toString());

        getEditText().addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                String result = onValidate(getEditText().getText().toString());
                if(result == null)
                {
                    getEditText().setError(null);
                    positiveButton.setEnabled(true);
                }
                else
                {
                    getEditText().setError(result);
                    positiveButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
    }

    /***
     * Called to validate contents of the edit text.
     *
     * Return null to indicate success, or return a validation error message to display on the edit text.
     *
     * @param text The text to validate.
     * @return An error message, or null if the value passes validation.
     */
    public abstract String onValidate(String text);
}