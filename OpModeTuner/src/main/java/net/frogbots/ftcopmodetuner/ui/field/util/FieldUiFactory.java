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

package net.frogbots.ftcopmodetuner.ui.field.util;

import net.frogbots.ftcopmodetuner.ui.field.BooleanFieldUi;
import net.frogbots.ftcopmodetuner.ui.field.ButtonFieldUi;
import net.frogbots.ftcopmodetuner.ui.field.ByteFieldUi;
import net.frogbots.ftcopmodetuner.ui.field.DoubleFieldUi;
import net.frogbots.ftcopmodetuner.ui.field.IntFieldUi;
import net.frogbots.ftcopmodetuner.ui.field.StringFieldUi;
import net.frogbots.ftcopmodetuner.ui.field.base.FieldInterface;
import net.frogbots.ftcopmodetuner.ui.field.base.FieldUi;
import net.frogbots.ftcopmodetuner.ui.field.FieldType;
import net.frogbots.ftcopmodetuner.ui.field.data.BooleanFieldData;
import net.frogbots.ftcopmodetuner.ui.field.data.ButtonFieldData;
import net.frogbots.ftcopmodetuner.ui.field.data.ByteFieldData;
import net.frogbots.ftcopmodetuner.ui.field.data.DoubleFieldData;
import net.frogbots.ftcopmodetuner.ui.field.data.FieldData;
import net.frogbots.ftcopmodetuner.ui.field.data.IntFieldData;
import net.frogbots.ftcopmodetuner.ui.field.data.StringFieldData;

public class FieldUiFactory
{
    public static FieldUi create(FieldType type, String tag, FieldInterface callback)
    {
        return create(type, null, tag, callback);
    }

    public static FieldUi create(FieldData data, FieldInterface callback)
    {
        return create(data.getType(), data, null, callback);
    }

    private static FieldUi create(FieldType type, FieldData data, String tag, FieldInterface callback)
    {
        if (type == FieldType.INT)
        {
            IntFieldUi fieldUi = new IntFieldUi(callback);

            if(data != null)
            {
                fieldUi.attachFieldDataClass((IntFieldData) data);
            }
            else
            {
                fieldUi.attachFieldDataClass(new IntFieldData(tag));
            }

            return fieldUi;
        }

        else if(type == FieldType.STRING)
        {
            StringFieldUi fieldUi = new StringFieldUi(callback);

            if(data != null)
            {
                fieldUi.attachFieldDataClass((StringFieldData) data);
            }
            else
            {
                fieldUi.attachFieldDataClass(new StringFieldData(tag));
            }

            return fieldUi;
        }

        else if(type == FieldType.BOOLEAN)
        {
            BooleanFieldUi fieldUi = new BooleanFieldUi(callback);

            if(data != null)
            {
                fieldUi.attachFieldDataClass((BooleanFieldData) data);
            }
            else
            {
                fieldUi.attachFieldDataClass(new BooleanFieldData(tag));
            }

            return fieldUi;
        }

        else if(type == FieldType.DOUBLE)
        {
            DoubleFieldUi fieldUi = new DoubleFieldUi(callback);

            if(data != null)
            {
                fieldUi.attachFieldDataClass((DoubleFieldData) data);
            }
            else
            {
                fieldUi.attachFieldDataClass(new DoubleFieldData(tag));
            }

            return fieldUi;
        }

        else if(type == FieldType.BYTE)
        {
            ByteFieldUi fieldUi = new ByteFieldUi(callback);

            if(data != null)
            {
                fieldUi.attachFieldDataClass((ByteFieldData) data);
            }
            else
            {
                fieldUi.attachFieldDataClass(new ByteFieldData(tag));
            }

            return fieldUi;
        }

        else if(type == FieldType.BUTTON)
        {
            ButtonFieldUi fieldUi = new ButtonFieldUi(callback);

            if(data != null)
            {
                fieldUi.attachFieldDataClass((ButtonFieldData) data);
            }
            else
            {
                fieldUi.attachFieldDataClass(new ButtonFieldData(tag));
            }

            return fieldUi;
        }

        throw new RuntimeException();
    }
}
