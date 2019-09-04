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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import net.frogbots.ftcopmodetuner.R;
import net.frogbots.ftcopmodetuner.ui.field.base.FieldInterface;
import net.frogbots.ftcopmodetuner.ui.field.FieldType;

/**
 * Dialog shown for creating a new Field
 */

public class NewFieldDialog extends AlertDialog.Builder
{
    private Spinner fieldTypeSpinner;
    private ArrayAdapter<CharSequence> fieldTypeAdapter;
    private TextView fieldTagTxtView;
    private FieldInterface fieldInterface;
    private Button positiveBtn;
    private boolean enableByte;
    private boolean enableButton;
    
    public NewFieldDialog(Context context, FieldInterface fieldInterface, boolean enableByte, boolean enableButton)
    {
        super(context);
        this.fieldInterface = fieldInterface;
        this.enableButton = enableButton;
        this.enableByte = enableByte;
    }

    @Override
    public AlertDialog show()
    {
        setTitle(R.string.add_new_field);
        LayoutInflater inflater = create().getLayoutInflater();

        FrameLayout frameView = new FrameLayout(getContext());
        setView(frameView);

        inflater.inflate(R.layout.new_field_dialog_layout, frameView);
        
        fieldTypeSpinner = frameView.findViewById(R.id.fieldTypeSpinner);
        fieldTagTxtView = frameView.findViewById(R.id.fieldTag);

        setupTypeSpinner();

        setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {

                String item = (String) fieldTypeSpinner.getSelectedItem();
                String tag = fieldTagTxtView.getText().toString();

                if(item.equals(getContext().getString(R.string.var_int))) //Int
                {
                    fieldInterface.addNewField(FieldType.INT, tag);
                }
                else if(item.equals(getContext().getString(R.string.var_double))) //Double
                {
                    fieldInterface.addNewField(FieldType.DOUBLE, tag);
                }
                else if(item.equals(getContext().getString(R.string.var_byte))) //Byte
                {
                    fieldInterface.addNewField(FieldType.BYTE, tag);
                }
                else if(item.equals(getContext().getString(R.string.var_string))) //String
                {
                    fieldInterface.addNewField(FieldType.STRING, tag);
                }
                else if(item.equals(getContext().getString(R.string.var_boolean))) //Boolean
                {
                    fieldInterface.addNewField(FieldType.BOOLEAN, tag);
                }
                else if(item.equals(getContext().getString(R.string.var_button)))
                {
                    fieldInterface.addNewField(FieldType.BUTTON, tag);
                }
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
                if (fieldTagTxtView.getText().toString().isEmpty())
                {
                    fieldTagTxtView.setError(null);
                    positiveBtn.setEnabled(false);
                }
                else if(fieldTagTxtView.getText().toString().startsWith(" "))
                {
                    fieldTagTxtView.setError(getContext().getString(R.string.leading_space));
                    positiveBtn.setEnabled(false);
                }
                else if(fieldTagTxtView.getText().toString().endsWith(" "))
                {
                    fieldTagTxtView.setError(getContext().getString(R.string.trailing_space));
                    positiveBtn.setEnabled(false);
                }
                else if(fieldTagTxtView.getText().toString().contains("  "))
                {
                    fieldTagTxtView.setError(getContext().getString(R.string.double_space));
                    positiveBtn.setEnabled(false);
                }
                else if(!fieldInterface.fieldTagAlreadyPresent(fieldTagTxtView.getText().toString(), null))
                {
                    fieldTagTxtView.setError(null);
                    positiveBtn.setEnabled(true);
                }
                else
                {
                    fieldTagTxtView.setError(getContext().getString(R.string.tag_in_use));
                    positiveBtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        return dialog;
    }

    private void setupTypeSpinner()
    {
        // Create an ArrayAdapter using the string array and a default spinner layout
        //fieldTypeAdapter = ArrayAdapter.createFromResource(getContext(), R.array.fieldTypeOptions, android.R.layout.simple_spinner_item);
        fieldTypeAdapter = new ArrayAdapter<CharSequence>(getContext(), android.R.layout.simple_spinner_item);

        fieldTypeAdapter.add(getContext().getString(R.string.var_double));
        fieldTypeAdapter.add(getContext().getString(R.string.var_boolean));
        fieldTypeAdapter.add(getContext().getString(R.string.var_int));
        fieldTypeAdapter.add(getContext().getString(R.string.var_string));

        if(enableByte)
        {
            fieldTypeAdapter.add(getContext().getString(R.string.var_byte));
        }

        if(enableButton)
        {
            fieldTypeAdapter.add(getContext().getString(R.string.var_button));
        }

        // Specify the layout to use when the list of choices appears
        fieldTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        fieldTypeSpinner.setAdapter(fieldTypeAdapter);
    }
}
