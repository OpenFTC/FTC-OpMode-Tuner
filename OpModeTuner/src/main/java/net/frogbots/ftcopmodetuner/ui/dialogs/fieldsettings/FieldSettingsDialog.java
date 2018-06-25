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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import net.frogbots.ftcopmodetuner.R;
import net.frogbots.ftcopmodetuner.ui.field.base.FieldInterface;
import net.frogbots.ftcopmodetuner.ui.field.base.FieldUi;

/**
 * Dialog shown for the Settings of a Field
 */

public class FieldSettingsDialog extends AlertDialog.Builder
{
    protected FieldUi field;
    private Button positiveBtn;
    private EditText key;
    private FieldInterface fieldInterface;
    private Button deleteButton;
    private AlertDialog dialog;
    View viewInflated;

    public FieldSettingsDialog(Context context, FieldInterface fieldInterface, FieldUi field)
    {
        super(context);

        this.fieldInterface = fieldInterface;
        this.field = field;
    }

    protected void setupUi()
    {
        viewInflated = create().getLayoutInflater().inflate(R.layout.field_settings_dialog, null, false);
        key = viewInflated.findViewById(R.id.fieldKey);
        key.setText(field.getData().tag);
        deleteButton = viewInflated.findViewById(R.id.deleteButton);
    }

    protected void onDialogPositiveBtnPressed()
    {
        fieldInterface.onRenameField(field.getData().tag, key.getText().toString());
    }

    protected void registerTextWatcherForEditTexts(TextWatcher watcher)
    {
        key.addTextChangedListener(watcher);
    }

    @Override
    public AlertDialog show()
    {
        setTitle(R.string.dialog_field_settings_title);
        setCancelable(false);
        setupUi();
        setView(viewInflated);

        setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();

                onDialogPositiveBtnPressed();
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

        dialog = super.show();

        positiveBtn = dialog.getButton(DialogInterface.BUTTON_POSITIVE);

        buttonThings();

        TextWatcher textWatcher = new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                buttonThings();
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        };

        registerTextWatcherForEditTexts(textWatcher);

        deleteButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                fieldInterface.removeField(field.getData().tag, false);
            }
        });

        return dialog;
    }

    public final void buttonThings()
    {
        boolean tagAlreadyPresentErr = false;
        boolean tagEmptyErr = false;

        tagAlreadyPresentErr = fieldInterface.fieldTagAlreadyPresent(key.getText().toString(), field);
        tagEmptyErr = key.getText().toString().isEmpty();

        /*
         * Handle error messages for the tag field
         */
        if(tagAlreadyPresentErr)
        {
            key.setError(getContext().getString(R.string.tag_in_use));
        }
        else if(tagEmptyErr)
        {
            key.setError(getContext().getString(R.string.tag_is_empty));
        }
        else
        {
            key.setError(null);
        }

        /*
         * If there was an error of any kind, then disable the OK button
         */
        if(tagAlreadyPresentErr || tagEmptyErr || otherErrors())
        {
            positiveBtn.setEnabled(false);
        }

        /*
         * There was NOT an error; enable the OK button
         */
        else
        {
            positiveBtn.setEnabled(true);
        }
    }

    protected boolean otherErrors()
    {
        return false;
    }
}
