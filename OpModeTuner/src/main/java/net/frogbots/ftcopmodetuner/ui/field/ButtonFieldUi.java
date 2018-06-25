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

import net.frogbots.ftcopmodetuner.R;
import net.frogbots.ftcopmodetuner.ui.field.base.FieldInterface;
import net.frogbots.ftcopmodetuner.ui.field.base.FieldUi;
import net.frogbots.ftcopmodetuner.ui.field.base.FieldUiInterface;
import net.frogbots.ftcopmodetunercommon.field.data.ButtonFieldData;
import net.frogbots.ftcopmodetunercommon.networking.datagram.ext.ButtonPressDatagram;

/**
 * This class handles the UI events of a ButtonField
 */

public class ButtonFieldUi extends FieldUi implements FieldUiInterface<ButtonFieldData>
{
    private ImageButton settingsBtn;
    private Button sendEventBtn;
    private ButtonFieldData data;

    public ButtonFieldUi(FieldInterface fieldInterface)
    {
        super(fieldInterface, R.layout.button_field_layout);
    }

    @Override
    public void setupUi(LayoutInflater inflater)
    {
        super.setupUi(inflater);

        settingsBtn = findViewById(R.id.fieldSettings);
        sendEventBtn = findViewById(R.id.sendEventBtn);

        settingsBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showFieldSettingsDialog();
            }
        });

        sendEventBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                addBtnPressEventToQueue(new ButtonPressDatagram(data.tag));
            }
        });
    }

    @Override
    public String getTypeString()
    {
        return "Button";
    }

    @Override
    public int getHeaderColorResId()
    {
        return 0;
    }

    @Override
    public void onManualInputReceived(String str)
    {

    }

    public void attachFieldDataClass(ButtonFieldData data)
    {
        this.data = data;
        internalAttachFieldDataClass(data);
    }
}
