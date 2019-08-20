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

package net.frogbots.ftcopmodetuner.ui.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import net.frogbots.ftcopmodetuner.R;
import net.frogbots.ftcopmodetunercommon.misc.DataConstants;

/**
 * Dialog shown for creating a new XML config file
 */

public class NewConfigDialog extends AlertDialog.Builder
{
    private TextView fieldTagTxtView;
    private Button positiveBtn;
    private NewConfigInterface newConfigInterface;

    public NewConfigDialog(Context context, NewConfigInterface newConfigInterface)
    {
        super(context, R.style.Theme_AppCompat_Light_Dialog_Alert);
        this.newConfigInterface = newConfigInterface;
    }

    @Override
    public AlertDialog show()
    {
        setTitle(R.string.add_new_config);
        LayoutInflater inflater = create().getLayoutInflater();

        FrameLayout frameView = new FrameLayout(getContext());
        setView(frameView);

        inflater.inflate(R.layout.new_config_dialog_layout, frameView);

        fieldTagTxtView = frameView.findViewById(R.id.configName);

        setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                String tag = fieldTagTxtView.getText().toString();
                newConfigInterface.addNewConfig(tag);
            }
        });

        setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {

            }
        });

        AlertDialog dialog = super.show();

        positiveBtn = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveBtn.setEnabled(false);

        fieldTagTxtView.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                String newTag = s.toString();

                positiveBtn.setEnabled(false);

                if (newTag.isEmpty())
                {
                    fieldTagTxtView.setError(null);
                }
                else if(newTag.startsWith(DataConstants.SPACE))
                {
                    fieldTagTxtView.setError(getContext().getString(R.string.leading_space));
                }
                else if(newTag.endsWith(DataConstants.SPACE))
                {
                    fieldTagTxtView.setError(getContext().getString(R.string.trailing_space));
                }
                else if(newTag.contains(DataConstants.DOUBLE_SPACE))
                {
                    fieldTagTxtView.setError(getContext().getString(R.string.double_space));
                }
                else if(newConfigInterface.configPresent(newTag))
                {
                    fieldTagTxtView.setError(getContext().getString(R.string.tag_in_use));
                }
                else
                {
                    fieldTagTxtView.setError(null);
                    positiveBtn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        return dialog;
    }
}
