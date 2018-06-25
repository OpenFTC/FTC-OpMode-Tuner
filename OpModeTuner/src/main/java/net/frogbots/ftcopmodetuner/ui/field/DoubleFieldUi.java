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
import net.frogbots.ftcopmodetunercommon.field.data.DoubleFieldData;
import net.frogbots.ftcopmodetunercommon.misc.DataConstants;

/**
 * This class handles the UI events of a DoubleField
 */

public class DoubleFieldUi extends FieldUi implements FieldUiInterface<DoubleFieldData>
{
    private Button btnKeyIn;
    private ImageButton settingsBtn;
    private SeekBar seekBar;
    private TextView value;
    private DoubleFieldData data;

    public DoubleFieldUi(FieldInterface fieldInterface)
    {
        super(fieldInterface, R.layout.number_layout);
    }

    private int getProgress()
    {
        return (int) (seekBar.getProgress() + (getMinData() * DataConstants.INT_TO_DOUBLE_SCALAR));
    }

    public void setMinMax(double min, double max)
    {
        setMaxData(max);
        setMinData(min);

        seekBar.setMax((int) ((max - min) * DataConstants.INT_TO_DOUBLE_SCALAR));
        seekBar.setProgress(1); // To trigger a call to the onChanged listener
        seekBar.setProgress(0);
        //seekBar.setMin(min);
    }

    private double getMinData()
    {
        return data.min;
    }

    private double getMaxData()
    {
        return data.max;
    }

    private void setMaxData(double max)
    {
        data.max = max;
    }

    private void setMinData(double min)
    {
        data.min = min;
    }

    private double getCurValue()
    {
        return data.curValue;
    }

    private void setCurValue(int value)
    {
        data.curValue = value / DataConstants.INT_TO_DOUBLE_SCALAR; //If seekbar is 1, then this will be .001
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
                setCurValue(getProgress());
                value.setText(String.valueOf(getCurValue()));
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
        double tmp = data.curValue;
        setMinMax(getMinData(), getMaxData());
        seekBar.setProgress((int) ((tmp - getMinData()) * DataConstants.INT_TO_DOUBLE_SCALAR));
    }

    @Override
    public String getTypeString()
    {
        return "Double";
    }

    @Override
    public int getHeaderColorResId()
    {
        return R.color.double_color;
    }

    @Override
    public void onManualInputReceived(String str)
    {
        value.setText(str);
        seekBar.setProgress((int) ((Double.parseDouble(str) - getMinData()) * DataConstants.INT_TO_DOUBLE_SCALAR));
    }

    public void attachFieldDataClass(DoubleFieldData data)
    {
        this.data = data;
        internalAttachFieldDataClass(data);
    }
}