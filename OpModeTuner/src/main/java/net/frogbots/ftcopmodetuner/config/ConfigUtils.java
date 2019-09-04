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

package net.frogbots.ftcopmodetuner.config;

import com.thoughtworks.xstream.XStream;

import net.frogbots.ftcopmodetuner.ui.activity.ConfigSelectionActivty;
import net.frogbots.ftcopmodetuner.ui.field.data.BooleanFieldData;
import net.frogbots.ftcopmodetuner.ui.field.data.ButtonFieldData;
import net.frogbots.ftcopmodetuner.ui.field.data.ByteFieldData;
import net.frogbots.ftcopmodetuner.ui.field.data.DoubleFieldData;
import net.frogbots.ftcopmodetuner.ui.field.data.FieldData;
import net.frogbots.ftcopmodetuner.ui.field.data.IntFieldData;
import net.frogbots.ftcopmodetuner.ui.field.data.StringFieldData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Utility class for loading and saving config files
 */

public class ConfigUtils
{
    private XStream xStream;

    public ConfigUtils()
    {
        xStream = new XStream();

        xStream.processAnnotations(Config.class);
        xStream.processAnnotations(FieldData.class);
        xStream.processAnnotations(DoubleFieldData.class);
        xStream.processAnnotations(IntFieldData.class);
        xStream.processAnnotations(StringFieldData.class);
        xStream.processAnnotations(BooleanFieldData.class);
        xStream.processAnnotations(ButtonFieldData.class);
        xStream.processAnnotations(ByteFieldData.class);
    }

    public ArrayList<FieldData> loadConfigByName(String name) throws FileNotFoundException, FileNotReadableException
    {
        File file = new File(String.format("%s/%s.xml", ConfigSelectionActivty.CONFIG_FILES_PATH, name));

        if(!file.exists())
        {
            throw new FileNotFoundException();
        }
        else if(!file.canRead())
        {
            throw new FileNotReadableException();
        }

        try
        {
            Config config = (Config) xStream.fromXML(file);

            if(config.getDataArray() == null)
            {
                config.setDataArray(new ArrayList<FieldData>());
            }

            return config.getDataArray();
        }
        catch (Exception e)
        {
            throw new FileNotReadableException();
        }
    }

    public void saveConfigToFileByName(String name, ArrayList<FieldData> data) throws IOException
    {
        File file = new File(String.format("%s/%s.xml", ConfigSelectionActivty.CONFIG_FILES_PATH, name));

        file.createNewFile();

        Config config = new Config(data);
        xStream.toXML(config, new FileOutputStream(file));
    }
}
