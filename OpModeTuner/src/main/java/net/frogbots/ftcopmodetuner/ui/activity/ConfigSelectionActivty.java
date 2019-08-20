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

package net.frogbots.ftcopmodetuner.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.frogbots.ftcopmodetuner.R;
import net.frogbots.ftcopmodetuner.config.ConfigUtils;
import net.frogbots.ftcopmodetuner.prefs.GlobalPrefs;
import net.frogbots.ftcopmodetuner.ui.dialogs.NewConfigDialog;
import net.frogbots.ftcopmodetuner.ui.dialogs.NewConfigInterface;
import net.frogbots.ftcopmodetunercommon.field.data.FieldData;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ConfigSelectionActivty extends Activity implements NewConfigInterface
{
    public static final String CONFIG_FILES_PATH = "/sdcard/FtcOpModeTuner";
    private LinearLayout mainLinearLayout;
    private Button addNewConfBtn;
    private TextView activeConfigTextView;
    private GlobalPrefs globalPrefs;
    private String activeConfig;
    private ConfigUtils configUtils;
    public static String PREF_KEY_ACTIVE_CONFIG = "activeConfig";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        configUtils = new ConfigUtils();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        globalPrefs = GlobalPrefs.getInstance();

        setContentView(R.layout.activity_config_selection_new);

        findViewById(R.id.available_configs_info_btn)
                .findViewById(R.id.info_btn)
                .setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showOkAlertDialog(
                        "Available configurations:",
                        "These are the configurations that were found in the /sdcard/FtcOpModeTuner folder.");
            }
        });

        findViewById(R.id.config_from_template_info_btn)
                .findViewById(R.id.info_btn)
                .setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        showOkAlertDialog(
                                "Configure from Template",
                                "Several configuration templates are available. Choosing one of these is the fastest way to start using the OpModeTuner.");
                    }
                });

        //getSupportActionBar().setTitle("Load Config");

        mainLinearLayout = findViewById(R.id.mainLinearLayout);
        addNewConfBtn = findViewById(R.id.new_button);
        activeConfigTextView = findViewById(R.id.idActiveConfigName);

        String activeConfPassedFromMainActivity = getIntent().getStringExtra(PREF_KEY_ACTIVE_CONFIG);
        if(activeConfPassedFromMainActivity == null)
        {
            activeConfigTextView.setText("NO CONFIG LOADED");
            activeConfig = "NO CONFIG LOADED";
        }
        else
        {
            activeConfigTextView.setText(activeConfPassedFromMainActivity);
            activeConfig = activeConfPassedFromMainActivity;
        }

        addNewConfBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new NewConfigDialog(ConfigSelectionActivty.this, ConfigSelectionActivty.this).show();
            }
        });

        File dir = new File(CONFIG_FILES_PATH);
        if(!dir.exists())
        {
            dir.mkdir();
        }

        addFileCards();
    }

    private void addFileCards()
    {
        mainLinearLayout.removeAllViews();

        for(final File f : loadFileList())
        {
            View view = getLayoutInflater().inflate(R.layout.config_layout, mainLinearLayout, false);
            TextView textView = view.findViewById(R.id.fieldKey);
            //Button deleteBtn

            textView.setText(f.getName().replace(".xml", ""));
            mainLinearLayout.addView(view);

            view.findViewById(R.id.load_button).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    activeConfig = f.getName().replace(".xml", "");
                    launchMainActivity(f);
                }
            });

            view.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    f.delete();
                    addFileCards();

                    if(!configPresent(activeConfig))
                    {
                        activeConfig = "NO CONFIG LOADED";
                        activeConfigTextView.setText(activeConfig);
                    }
                }
            });
        }
    }

    private void launchMainActivity(File file)
    {
        globalPrefs.putString(R.string.prefkey_activeConfig, file.getName().replace(".xml", "")).apply();

        Intent returnIntent = new Intent().putExtra(PREF_KEY_ACTIVE_CONFIG, activeConfig);
        setResult(Activity.RESULT_OK, returnIntent);

        finish();
    }

    private ArrayList<File> loadFileList()
    {
        ArrayList<File> configFiles = new ArrayList<>();
        File[] files = new File(CONFIG_FILES_PATH).listFiles();

        for(File f : files)
        {
            if(f.getName().endsWith(".xml"))
            {
                configFiles.add(f);
            }
        }

        return configFiles;
    }

    @Override
    public boolean configPresent(String name)
    {
        for(File f : loadFileList())
        {
            if(f.getName().replace(".xml", "").equals(name))
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public void addNewConfig(String name)
    {
        try
        {
            configUtils.saveConfigToFileByName(name, new ArrayList<FieldData>());
            addFileCards();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void onNewButtonPressed(View v)
    {

    }

    public void showOkAlertDialog(String title, String msg)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });
        builder.show();
    }
}
