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

package net.frogbots.ftcopmodetuner.ui.field.base;

import android.support.annotation.IdRes;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.frogbots.ftcopmodetuner.R;
import net.frogbots.ftcopmodetuner.ui.field.data.ButtonFieldData;
import net.frogbots.ftcopmodetuner.ui.field.data.FieldData;
import net.frogbots.ftcopmodetunercommon.networking.datagram.ext.ButtonPressDatagram;

/**
 * This class handles the UI events of a Field
 */

abstract public class FieldUi implements FieldUiInterfaceBase
{
    private TextView fieldKeyTxtView;
    private CardView cardView;
    private TextView typeTxtView;
    private int layoutId;
    private FieldInterface fieldInterface;
    public View view;
    private FieldData data;

    protected FieldUi(FieldInterface fieldInterface, int layoutId)
    {
        this.fieldInterface = fieldInterface;
        this.layoutId = layoutId;
    }

    protected void internalAttachFieldDataClass(FieldData data)
    {
        this.data = data;
    }

    public FieldData getData()
    {
        return data;
    }

    public View createView(LayoutInflater inflater, ViewGroup parent)
    {
        view = inflater.inflate(R.layout.field_layout, parent, false);

        setupUi(inflater);

        return view;
    }

    public void setColorCodingAndDatatypeDisplay(boolean color, boolean datatype)
    {
        LinearLayout colorStrip = view.findViewById(R.id.colorStrip);
        colorStrip.setBackgroundResource(getHeaderColorResId());

        if(color)
        {
            colorStrip.setVisibility(View.VISIBLE);
        }
        else
        {
            colorStrip.setVisibility(View.GONE);
        }

        if(datatype)
        {
            typeTxtView.setVisibility(View.VISIBLE);
        }
        else
        {
            typeTxtView.setVisibility(View.INVISIBLE);
        }
    }

    public void setupUi(LayoutInflater inflater)
    {
        LinearLayout layout = view.findViewById(R.id.fieldCardMainLayout);
        inflater.inflate(layoutId, layout, true);

        cardView = view.findViewById(R.id.card);

        fieldKeyTxtView = view.findViewById(R.id.fieldKey);
        fieldKeyTxtView.setText(data.tag);
        typeTxtView = findViewById(R.id.fieldType);
        typeTxtView.setText(getTypeString());
        cardView.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                fieldInterface.removeField(data.tag, true);
                return false;
            }
        });

        /*
         * Buttons are special
         */
        if(!(data instanceof ButtonFieldData))
        {
            cardView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    fieldInterface.onShowAlertDialogForCodeSampleRequested(
                            "Access this field with",
                            String.format("get%s(\"%s\");", getTypeString(), data.tag));
                }
            });
        }
    }

    /*
     * Just a short hand so we don't have to do view.findView...
     */
    protected <T extends View> T findViewById(@IdRes int id)
    {
        return view.findViewById(id);
    }

    public void rename(String newTag)
    {
        data.tag = newTag;
        fieldKeyTxtView.setText(newTag);
    }

    protected void showFieldSettingsDialog()
    {
        fieldInterface.onShowFieldSettingsDialogRequested(this);
    }

    protected void showToast(String string)
    {
        fieldInterface.onShowToastRequested(string);
    }

    protected void requestManualInput()
    {
        fieldInterface.onManualInputRequested(this);
    }

    protected void addBtnPressEventToQueue(ButtonPressDatagram datagram)
    {
        fieldInterface.addBtnPressEventToQueue(datagram);
    }
}
