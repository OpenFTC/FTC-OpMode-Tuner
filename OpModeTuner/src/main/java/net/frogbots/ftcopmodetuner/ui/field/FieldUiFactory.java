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

import net.frogbots.ftcopmodetunercommon.field.FieldType;
import net.frogbots.ftcopmodetunercommon.field.data.BooleanFieldData;
import net.frogbots.ftcopmodetunercommon.field.data.ButtonFieldData;
import net.frogbots.ftcopmodetunercommon.field.data.ByteFieldData;
import net.frogbots.ftcopmodetunercommon.field.data.DoubleFieldData;
import net.frogbots.ftcopmodetunercommon.field.data.FieldData;
import net.frogbots.ftcopmodetunercommon.field.data.IntFieldData;
import net.frogbots.ftcopmodetunercommon.field.data.StringFieldData;

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
        FieldUi fieldUi = null;

        if (type == FieldType.INT)
        {
            fieldUi = new IntFieldUi(callback);

            if(data != null)
            {
                fieldUi.attachFieldDataClass(data);
            }
            else
            {
                fieldUi.attachFieldDataClass(new IntFieldData(tag));
            }
        }

        else if(type == FieldType.STRING)
        {
            fieldUi = new StringFieldUi(callback);

            if(data != null)
            {
                fieldUi.attachFieldDataClass(data);
            }
            else
            {
                fieldUi.attachFieldDataClass(new StringFieldData(tag));
            }
        }

        else if(type == FieldType.BOOLEAN)
        {
            fieldUi = new BooleanFieldUi(callback);

            if(data != null)
            {
                fieldUi.attachFieldDataClass(data);
            }
            else
            {
                fieldUi.attachFieldDataClass(new BooleanFieldData(tag));
            }
        }

        else if(type == FieldType.DOUBLE)
        {
            fieldUi = new DoubleFieldUi(callback);

            if(data != null)
            {
                fieldUi.attachFieldDataClass(data);
            }
            else
            {
                fieldUi.attachFieldDataClass(new DoubleFieldData(tag));
            }
        }

        else if(type == FieldType.BYTE)
        {
            fieldUi = new ByteFieldUi(callback);

            if(data != null)
            {
                fieldUi.attachFieldDataClass(data);
            }
            else
            {
                fieldUi.attachFieldDataClass(new ByteFieldData(tag));
            }
        }

        else if(type == FieldType.BUTTON)
        {
            fieldUi = new ButtonFieldUi(callback);

            if(data != null)
            {
                fieldUi.attachFieldDataClass(data);
            }
            else
            {
                fieldUi.attachFieldDataClass(new ButtonFieldData(tag));
            }
        }

        return fieldUi;
    }
}
