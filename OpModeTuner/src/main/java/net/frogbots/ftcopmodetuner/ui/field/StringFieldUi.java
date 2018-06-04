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

package net.frogbots.ftcopmodetuner.ui.field;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import net.frogbots.ftcopmodetuner.R;
import net.frogbots.ftcopmodetunercommon.field.data.FieldData;
import net.frogbots.ftcopmodetunercommon.field.data.StringFieldData;

/**
 * This class handles the UI events of a StringField
 */

public class StringFieldUi extends FieldUi
{
    private ImageButton settingsBtn;
    private EditText editText;

    public StringFieldUi(LayoutInflater layoutInflater, FieldInterface fieldInterface, ViewGroup parent)
    {
        super(layoutInflater, fieldInterface, parent, R.layout.string_layout);
    }

    @Override
    public void setupUi()
    {
        super.setupUi();

        editText = findViewById(R.id.stringField);
        settingsBtn = findViewById(R.id.fieldSettings);

        settingsBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showFieldSettingsDialog();
            }
        });

        editText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                setCurValue(editText.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        /*
         * To ensure the UI gets restored properly
         * when loading from XML
         */
        onManualInputReceived(((StringFieldData)data).string);
    }

    @Override
    public String getTypeString()
    {
        return "String";
    }

    @Override
    public int getHeaderColorResId()
    {
        return R.color.string_color;
    }

    private void setCurValue(String string)
    {
        ((StringFieldData)data).string = string;
    }

    @Override
    public void onManualInputReceived(String str)
    {
        setCurValue(str);
        editText.setText(str);
    }

    @Override
    public void attachFieldDataClass(FieldData data)
    {
        if(data instanceof StringFieldData)
        {
            super.attachFieldDataClass(data);
        }
        else
        {
            throw new IllegalArgumentException("Can't a attach a non-StringFieldData class to a StringFieldUi!");
        }
    }
}
