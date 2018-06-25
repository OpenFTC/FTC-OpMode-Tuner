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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import net.frogbots.ftcopmodetuner.R;
import net.frogbots.ftcopmodetuner.ui.field.base.FieldUi;

/**
 * Dialog shown for manually entering data to a Field
 */

public abstract class ManualKeyInDialog extends AlertDialog.Builder
{
    FieldUi fieldUi;
    Button doneBtn;
    EditText input;

    public ManualKeyInDialog(Context context, FieldUi fieldUi)
    {
        super(context);
        this.fieldUi = fieldUi;
    }

    protected abstract void setup();

    @Override
    public AlertDialog show()
    {
        setTitle(R.string.dialog_manual_key_in_title);
        setCancelable(false);
        View viewInflated = create().getLayoutInflater().inflate(R.layout.custom_input_dialog_layout, null, false);

        input = viewInflated.findViewById(R.id.input);

        setup();

        setView(viewInflated);

        setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                fieldUi.onManualInputReceived(input.getText().toString());
            }
        });
        setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });

        AlertDialog dialog = super.show();
        doneBtn = dialog.getButton(DialogInterface.BUTTON_POSITIVE);

        input.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                validate();
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        return dialog;
    }

    protected abstract void validate();
}
