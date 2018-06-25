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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import net.frogbots.ftcopmodetuner.R;
import net.frogbots.ftcopmodetuner.ui.field.base.FieldInterface;
import net.frogbots.ftcopmodetuner.ui.field.base.FieldUi;
import net.frogbots.ftcopmodetuner.ui.field.base.FieldUiInterface;
import net.frogbots.ftcopmodetunercommon.field.data.IntFieldData;

/**
 * This class handles the UI events of an IntField
 */

public class IntFieldUi extends FieldUi implements FieldUiInterface<IntFieldData>
{
    private Button btnKeyIn;
    private ImageButton settingsBtn;
    private SeekBar seekBar;
    private TextView value;
    private IntFieldData data;

    public IntFieldUi(FieldInterface fieldInterface)
    {
        super(fieldInterface, R.layout.number_layout);
    }

    private int getProgress()
    {
        return seekBar.getProgress() + getMinData();
    }

    public void setMinMax(int min, int max)
    {
        setMaxData(max);
        setMinData(min);

        seekBar.setMax(max - min);
        seekBar.setProgress(1); // To trigger a call to the onChanged listener
        seekBar.setProgress(0);
        //seekBar.setMin(min);
    }

    private int getMinData()
    {
        return data.min;
    }

    private int getMaxData()
    {
        return data.max;
    }

    private void setMaxData(int max)
    {
        data.max = max;
    }

    private void setMinData(int min)
    {
        data.min = min;
    }

    private void setCurValue(int value)
    {
        data.curValue = value;
    }

    @Override
    public void setupUi(LayoutInflater inflater)
    {
        super.setupUi(inflater);

        settingsBtn = findViewById(R.id.fieldSettings);
        btnKeyIn = findViewById(R.id.btnKeyIn);
        seekBar = findViewById(R.id.seekBar);
        value = findViewById(R.id.value);

        value.setText(String.valueOf(seekBar.getProgress()));

        settingsBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showFieldSettingsDialog();
            }
        });

        btnKeyIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                requestManualInput();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                value.setText(String.valueOf(getProgress()));
                setCurValue(getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });

        /*
         * To ensure the UI gets restored properly
         * when loading from XML
         */
        int tmp = data.curValue;
        setMinMax(getMinData(), getMaxData());
        seekBar.setProgress(tmp - getMinData());
    }

    @Override
    public String getTypeString()
    {
        return "Integer";
    }

    @Override
    public int getHeaderColorResId()
    {
        return R.color.integer_color;
    }

    @Override
    public void onManualInputReceived(String str)
    {
        value.setText(str);
        seekBar.setProgress(Integer.parseInt(str) - getMinData());
    }

    public void attachFieldDataClass(IntFieldData data)
    {
        this.data = data;
        internalAttachFieldDataClass(data);
    }
}
