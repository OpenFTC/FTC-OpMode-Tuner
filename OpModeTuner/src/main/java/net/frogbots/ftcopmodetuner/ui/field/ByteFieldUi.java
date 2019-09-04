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
import android.widget.EditText;
import android.widget.ImageButton;

import net.frogbots.ftcopmodetuner.R;
import net.frogbots.ftcopmodetuner.ui.field.base.FieldInterface;
import net.frogbots.ftcopmodetuner.ui.field.base.FieldUi;
import net.frogbots.ftcopmodetuner.ui.field.base.FieldUiInterface;
import net.frogbots.ftcopmodetuner.ui.field.data.ByteFieldData;
import net.frogbots.ftcopmodetunercommon.misc.DatatypeUtil;

/**
 * This class handles the UI events of a ByteField
 */

public class ByteFieldUi extends FieldUi implements FieldUiInterface<ByteFieldData>
{
    private Button btnKeyIn;
    private ImageButton settingsBtn;
    private EditText textView;
    private ByteFieldData data;

    public ByteFieldUi(FieldInterface fieldInterface)
    {
        super(fieldInterface, R.layout.byte_layout);
    }

    @Override
    public void setupUi(LayoutInflater inflater)
    {
        super.setupUi(inflater);

        textView = findViewById(R.id.byteField);
        btnKeyIn = findViewById(R.id.btnKeyIn);
        settingsBtn = findViewById(R.id.fieldSettings);

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

        textView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showToast("Use the edit button!");
            }
        });

        /*
         * To ensure the UI gets restored properly
         * when loading from XML
         */
        onManualInputReceived(DatatypeUtil.byteToHex(data.value));
    }

    private void setCurValue(byte b)
    {
        data.value = b;
    }

    @Override
    public void onManualInputReceived(String str)
    {
        setCurValue(DatatypeUtil.hexToByte(str));
        textView.setText(str);
    }

    @Override
    public String getTypeString()
    {
        return "Byte";
    }

    @Override
    public int getHeaderColorResId()
    {
        return R.color.byte_color;
    }

    public void attachFieldDataClass(ByteFieldData data)
    {
        this.data = data;
        internalAttachFieldDataClass(data);
    }
}
