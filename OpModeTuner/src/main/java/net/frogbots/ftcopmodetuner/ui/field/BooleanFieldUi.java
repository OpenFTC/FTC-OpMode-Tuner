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

import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import net.frogbots.ftcopmodetuner.R;
import net.frogbots.ftcopmodetuner.ui.field.base.FieldInterface;
import net.frogbots.ftcopmodetuner.ui.field.base.FieldUi;
import net.frogbots.ftcopmodetuner.ui.field.base.FieldUiInterface;
import net.frogbots.ftcopmodetuner.ui.field.data.BooleanFieldData;

/**
 * This class handles the UI events of a BooleanField
 */

public class BooleanFieldUi extends FieldUi implements FieldUiInterface<BooleanFieldData>
{
    private Switch switchBtn;
    private TextView textView;
    private ImageButton settingsBtn;
    private BooleanFieldData data;

    public BooleanFieldUi(FieldInterface fieldInterface)
    {
        super(fieldInterface, R.layout.boolean_layout);
    }

    private void setCurValue(boolean b)
    {
        data.value = b;
    }

    @Override
    public void setupUi(LayoutInflater inflater)
    {
        super.setupUi(inflater);

        switchBtn = findViewById(R.id.switchBtn);
        textView = findViewById(R.id.value);
        settingsBtn = findViewById(R.id.fieldSettings);

        switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                updateState(isChecked);
            }
        });

        settingsBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showFieldSettingsDialog();
            }
        });

        /*
         * To ensure the UI gets restored properly
         * when loading from XML
         */
        updateState(data.value, true);
    }

    private void updateState(boolean b)
    {
        updateState(b, false);
    }

    private void updateState(boolean b, boolean fromXmlInit)
    {
        setCurValue(b);
        textView.setText(String.valueOf(b));

        if(fromXmlInit)
        {
            if(switchBtn.isChecked() != b)
            {
                switchBtn.setChecked(b);
            }
        }
    }

    @Override
    public String getTypeString()
    {
        return "Boolean";
    }

    @Override
    public int getHeaderColorResId()
    {
        return R.color.boolean_color;
    }

    @Override
    public void onManualInputReceived(String str)
    {

    }

    public void attachFieldDataClass(BooleanFieldData data)
    {
        this.data = data;
        internalAttachFieldDataClass(data);
    }
}
