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
import net.frogbots.ftcopmodetuner.ui.field.util.SignedSeekBar;
import net.frogbots.ftcopmodetunercommon.field.data.IntFieldData;

/**
 * This class handles the UI events of an IntField
 */

public class IntFieldUi extends FieldUi implements FieldUiInterface<IntFieldData>, SignedSeekBar.OnSignedSeekBarChangeListener, View.OnClickListener
{
    private Button btnKeyIn;
    private ImageButton settingsBtn;
    private SignedSeekBar seekBar;
    private TextView value;
    private IntFieldData data;

    public IntFieldUi(FieldInterface fieldInterface)
    {
        super(fieldInterface, R.layout.number_layout);
    }

    public void setMinMax(int min, int max)
    {
        //Cache the current value
        int valBeforeSetMinMax = data.curValue;

        data.min = min;
        data.max = max;

        seekBar.setMinMax(min, max);

        /*
         * If possible, restore the previous value
         */
        if(valBeforeSetMinMax >= min && valBeforeSetMinMax <= max)
        {
            seekBar.setProgress(valBeforeSetMinMax);
        }
    }

    @Override
    public void setupUi(LayoutInflater inflater)
    {
        super.setupUi(inflater);

        /*
         * Create our UI handles
         */
        settingsBtn = findViewById(R.id.fieldSettings);
        btnKeyIn = findViewById(R.id.btnKeyIn);
        seekBar = new SignedSeekBar((SeekBar) findViewById(R.id.seekBar), data.min, data.max);
        value = findViewById(R.id.value);

        /*
         * Set our listeners
         */
        settingsBtn.setOnClickListener(this);
        btnKeyIn.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);

        /*
         * Restore UI state
         */
        seekBar.setProgress(data.curValue);
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
        seekBar.setProgress(Integer.parseInt(str));
    }

    public void attachFieldDataClass(IntFieldData data)
    {
        this.data = data;
        internalAttachFieldDataClass(data);
    }

    @Override
    public void onProgressChanged(SignedSeekBar seekBar, int progress, boolean fromUser)
    {
        value.setText(String.valueOf(progress));
        data.curValue = progress;
    }

    @Override
    public void onClick(View view)
    {
        if(view == settingsBtn)
        {
            showFieldSettingsDialog();
        }
        else if(view == btnKeyIn)
        {
            requestManualInput();
        }
    }
}
