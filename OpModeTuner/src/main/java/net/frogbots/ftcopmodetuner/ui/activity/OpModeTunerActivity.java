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
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.frogbots.ftcopmodetuner.R;
import net.frogbots.ftcopmodetuner.config.ConfigUtils;
import net.frogbots.ftcopmodetuner.config.FileNotReadableException;
import net.frogbots.ftcopmodetuner.ui.field.base.FieldInterface;
import net.frogbots.ftcopmodetuner.ui.field.ButtonFieldUi;
import net.frogbots.ftcopmodetuner.ui.field.ByteFieldUi;
import net.frogbots.ftcopmodetuner.ui.field.DoubleFieldUi;
import net.frogbots.ftcopmodetuner.ui.field.base.FieldUi;
import net.frogbots.ftcopmodetuner.ui.field.util.FieldUiFactory;
import net.frogbots.ftcopmodetuner.ui.field.IntFieldUi;
import net.frogbots.ftcopmodetuner.prefs.GlobalPrefs;
import net.frogbots.ftcopmodetuner.prefs.PrefKeys;
import net.frogbots.ftcopmodetuner.ui.activity.settings.OpModeTunerSettingsActivity;
import net.frogbots.ftcopmodetuner.ui.dialogs.NewFieldDialog;
import net.frogbots.ftcopmodetuner.ui.dialogs.fieldsettings.DoubleFieldSettingsDialog;
import net.frogbots.ftcopmodetuner.ui.dialogs.fieldsettings.FieldSettingsDialog;
import net.frogbots.ftcopmodetuner.ui.dialogs.fieldsettings.IntFieldSettingsDialog;
import net.frogbots.ftcopmodetuner.ui.dialogs.keyindialog.ByteKeyInDialog;
import net.frogbots.ftcopmodetuner.ui.dialogs.keyindialog.DoubleKeyInDialog;
import net.frogbots.ftcopmodetuner.ui.dialogs.keyindialog.IntKeyInDialog;
import net.frogbots.ftcopmodetunercommon.field.FieldType;
import net.frogbots.ftcopmodetunercommon.field.data.FieldData;
import net.frogbots.ftcopmodetunercommon.misc.DataConstants;
import net.frogbots.ftcopmodetunercommon.networking.datagram.Datagram;
import net.frogbots.ftcopmodetunercommon.networking.datagram.array.DatagramArrayEncoder;
import net.frogbots.ftcopmodetunercommon.networking.datagram.ext.ButtonPressDatagram;
import net.frogbots.ftcopmodetunercommon.networking.udp.ConnectionStatus;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static net.frogbots.ftcopmodetuner.ui.activity.ConfigSelectionActivty.PREF_KEY_ACTIVE_CONFIG;

public class OpModeTunerActivity extends UdpConnectionActivity implements FieldInterface
{
    private LinearLayout mainLinearLayout;
    private ArrayList<FieldUi> fields = new ArrayList<>();
    private ArrayList<ButtonPressDatagram> btnPressDatagramQueue = new ArrayList<>();
    private GlobalPrefs globalPrefs;
    private int LOAD_CONFIG_INTENT_ID = 1;
    private boolean colorCoding;
    private boolean displayDatatype;
    private boolean deleteFieldOnLongPress;
    private boolean enableByteDataType;
    private boolean enableButtonDataType;
    private boolean loadLastConfigOnStartup;
    private FloatingActionButton addNewFieldBtn;
    private String currentConfig;
    private TextView activeConfigTxtView;
    private ConfigUtils configUtils;
    private ScheduledExecutorService transmissionTimer;
    private Runnable transmissionRunnable;
    private int transmissionInterval;

    //--------------------------------------------------------------------------------------------------
    // Android Lifecycle
    //--------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        globalPrefs = GlobalPrefs.getInstance();

        configUtils = new ConfigUtils();

        mainLinearLayout = findViewById(R.id.mainLinearLayout);
        addNewFieldBtn = findViewById(R.id.addNewFieldBtn);
        addThingBelowConnectionStatusView(R.layout.active_config_box);
        activeConfigTxtView = findViewById(R.id.active_config);

        if(savedInstanceState != null)
        {
            FieldData[] dataArray = (FieldData[]) savedInstanceState.getParcelableArray("dataArray");

            for(FieldData d : dataArray)
            {
                addFieldFromSavedBundle(d);
            }
        }

        updatePrefVals();

        if(loadLastConfigOnStartup)
        {
            System.out.println(globalPrefs.getString(PrefKeys.ACTIVE_CONFIG, null));
            loadConfig(globalPrefs.getString(PrefKeys.ACTIVE_CONFIG, null));
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();

        transmissionTimer.shutdown();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        updatePrefVals();
        setColorCodingAndDatatypeForAll();
        setupTransmissionRunnable();
    }

    @Override
    public void onSaveInstanceState(Bundle bundle)
    {
        super.onSaveInstanceState(bundle);

        ArrayList<FieldData> dataArray = new ArrayList<>();

        for (FieldUi f : fields)
        {
            dataArray.add(f.getData());
        }

        bundle.putParcelableArray("dataArray", dataArray.toArray(new FieldData[dataArray.size()]));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == LOAD_CONFIG_INTENT_ID)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                String result = data.getStringExtra(PREF_KEY_ACTIVE_CONFIG);
                System.out.println(result);

                unloadCurrentConfig();
                loadConfig(result);
            }
        }
    }

    //--------------------------------------------------------------------------------------------------
    // Overflow Menu
    //--------------------------------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.opmodetuner_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        /*
         * A menu item was pressed; figure out which one
         * and take the appropriate action
         */

        if (id == R.id.settingsMenuItem)
        {
            Intent intent = new Intent(this, OpModeTunerSettingsActivity.class);
            startActivity(intent);

            return true;
        }

        else if (id == R.id.aboutMenuItem)
        {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        else if (id == R.id.clearMenuItem)
        {
            clearAllFieldsWithConfirmationDialog();
        }

        else if(id == R.id.loadConfigMenuItem)
        {
            Intent intent = new Intent(this, ConfigSelectionActivty.class);
            intent.putExtra(PREF_KEY_ACTIVE_CONFIG, currentConfig);
            startActivityForResult(intent, LOAD_CONFIG_INTENT_ID);
        }

        else if(id == R.id.saveConfigMenuItem)
        {
            saveCurrentFieldArrayToConfig();
        }

        return super.onOptionsItemSelected(item);
    }

    //--------------------------------------------------------------------------------------------------
    // UI Stuff
    //--------------------------------------------------------------------------------------------------

    private void setColorCodingAndDatatypeForAll()
    {
        for(FieldUi f : fields)
        {
            f.setColorCodingAndDatatypeDisplay(colorCoding, displayDatatype);
        }
    }

    public void showAddFieldDialog(View v)
    {
        if(currentConfig != null)
        {
            new NewFieldDialog(this, this, enableByteDataType, enableButtonDataType).show();
        }
        else
        {
            showAlertDialog("No config loaded", "You need to load a config before adding fields.");
        }
    }

    /***
     * Just a UI hook to show a toast
     *
     * @param str show this text in the toast
     */
    @Override
    public void onShowToastRequested(String str)
    {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    public void showAlertDialog(String title, String msg, Typeface typeface)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(title);
        builder.setMessage(msg);

        AlertDialog d = builder.show();

        TextView messageView = d.findViewById(android.R.id.message);
        messageView.setTypeface(typeface);
    }

    public void showAlertDialog(String title, String msg)
    {
        showAlertDialog(title, msg, Typeface.DEFAULT);
    }

    @Override
    public void onShowAlertDialogForCodeSampleRequested(String title, String msg)
    {
        showAlertDialog(title, msg, Typeface.MONOSPACE);
    }

    //--------------------------------------------------------------------------------------------------
    // Fields
    //--------------------------------------------------------------------------------------------------

    @Override
    public void addNewField(FieldType fieldType, String tag)
    {
        FieldUi fieldUi = FieldUiFactory.create(fieldType, tag, this);
        View view = fieldUi.createView(getLayoutInflater(), mainLinearLayout);
        fieldUi.setColorCodingAndDatatypeDisplay(colorCoding, displayDatatype);

        mainLinearLayout.addView(view);
        fields.add(fieldUi);
    }

    public void addFieldFromSavedBundle(FieldData data)
    {
        FieldUi fieldUi = FieldUiFactory.create(data, this);
        View view = fieldUi.createView(getLayoutInflater(), mainLinearLayout);

        mainLinearLayout.addView(view);
        fields.add(fieldUi);
    }

    /***
     * Removes a field from both the UI and the data array
     *
     * @param tag the tag of the field to remove
     * @param longPress indicates whether or not this remove event was triggered by a long press
     */
    @Override
    public void removeField(final String tag, boolean longPress)
    {
        if(!longPress || deleteFieldOnLongPress)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.dialog_remove_field_title);
            builder.setMessage(String.format(Locale.US, getString(R.string.dialog_remove_field_msg), tag));
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {

                }
            });
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    mainLinearLayout.removeView(findFieldByTag(tag).view);
                    fields.remove(findFieldByTag(tag));
                }
            });
            builder.setCancelable(false);
            builder.show();
        }
    }

    @Override
    public void onManualInputRequested(FieldUi fieldUi)
    {
        if(fieldUi instanceof DoubleFieldUi)
        {
            new DoubleKeyInDialog(this, fieldUi).show();
        }
        else if(fieldUi instanceof IntFieldUi)
        {
            new IntKeyInDialog(this, fieldUi).show();
        }
        else if(fieldUi instanceof ByteFieldUi)
        {
            new ByteKeyInDialog(this, fieldUi).show();
        }
    }

    @Override
    public void addBtnPressEventToQueue(ButtonPressDatagram datagram)
    {
        btnPressDatagramQueue.add(datagram);
    }

    @Override
    public void onShowFieldSettingsDialogRequested(FieldUi fieldUi)
    {
        if(fieldUi instanceof DoubleFieldUi)
        {
            new DoubleFieldSettingsDialog(this, this, fieldUi).show();
        }
        else if(fieldUi instanceof IntFieldUi)
        {
            new IntFieldSettingsDialog(this, this, fieldUi).show();
        }
        else
        {
            new FieldSettingsDialog(this, this, fieldUi).show();
        }
    }

    @Override
    public void onRenameField(String currTag, String newTag)
    {
        findFieldByTag(currTag).rename(newTag);
    }

    /***
     * Searches all the currently added fields to find out if the requested tag is already taken
     *
     * @param tag the tag we're checking to see if already exists
     * @param field needed so we don't return true based on itself
     * @return whether the tag is already taken
     */
    @Override
    public boolean fieldTagAlreadyPresent(String tag, FieldUi field)
    {
        for (FieldUi f : fields)
        {
            if(f.getData().tag.equals(tag) && !f.equals(field))
            {
                return true;
            }
        }

        return false;
    }

    private void clearAllFields()
    {
        for(FieldUi f : fields)
        {
            mainLinearLayout.removeView(f.view);
        }

        fields.clear();
    }

    private void clearAllFieldsWithConfirmationDialog()
    {
        /*
         * Don't show a dialog if there aren't even any fields
         */
        if(fields.size() > 0)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.clear_all_fields_dialog_title);
            builder.setMessage(R.string.clear_all_fields_dialog_text);
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {

                }
            });
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    /*
                     * Alright, they confirmed, nuke everything from orbit
                     */
                    clearAllFields();
                }
            });
            builder.setCancelable(false);
            builder.show();
        }
    }

    /***
     * Find the FieldUi object matching a given tag
     *
     * @param tag the tag of the FieldUi object we're looking for
     * @return the FieldUi object with the matching tag
     */
    private FieldUi findFieldByTag(String tag)
    {
        for(FieldUi f : fields)
        {
            if(f.getData().tag.equals(tag))
            {
                return f;
            }
        }

        throw new RuntimeException();
    }

    private void unloadCurrentConfig()
    {
        currentConfig = null;
        clearAllFields();
    }

    private void loadConfig(String confName)
    {
        try
        {
            ArrayList<FieldData> arrFromConf = configUtils.loadConfigByName(confName);
            unloadCurrentConfig();

            for(FieldData fd : arrFromConf)
            {
                addFieldFromSavedBundle(fd);
            }

            activeConfigTxtView.setText("Active config: " + confName);
            currentConfig = confName;
        }
        catch (FileNotReadableException | FileNotFoundException e)
        {
            currentConfig = null;
            onShowToastRequested("Failed to load config!");
            activeConfigTxtView.setText("Active config: NO CONFIG LOADED");

            e.printStackTrace();
        }
    }

    private void saveCurrentFieldArrayToConfig()
    {
        if(currentConfig == null)
        {
            onShowToastRequested("Nothing to save...!");
            return;
        }

        ArrayList<FieldData> arr = new ArrayList<>();
        for(FieldUi fui : fields)
        {
            arr.add(fui.getData());
        }

        try
        {
            configUtils.saveConfigToFileByName(currentConfig, arr);
            onShowToastRequested("Config saved successfully!");
        } catch (IOException e)
        {
            currentConfig = null;
            onShowToastRequested("Failed to save config!");

            e.printStackTrace();
        }
    }

    //--------------------------------------------------------------------------------------------------
    // Networking
    //--------------------------------------------------------------------------------------------------

    private void setupTransmissionRunnable()
    {
        transmissionRunnable = new Runnable()
        {
            @Override
            public void run()
            {
                if(networkingManager.getConnectionStatus() == ConnectionStatus.CONNECTED)
                {
                    networkingManager.sendBytes(getDataForTransmissionToRC());
                }
            }
        };

        transmissionTimer = Executors.newSingleThreadScheduledExecutor();
        transmissionTimer.scheduleAtFixedRate(transmissionRunnable, 0, transmissionInterval, TimeUnit.MILLISECONDS);
    }

    private byte[] getDataForTransmissionToRC()
    {
        if(currentConfig == null)
        {
            return null;
        }

        ArrayList<Datagram> datagrams = new ArrayList<>();

        for(FieldUi f : fields)
        {
            /*
             * Don't add button fields, they're special
             * and are transmitted separately
             */
            if(!(f instanceof ButtonFieldUi))
            {
                datagrams.add(f.getData().toDatagram());
            }
        }

        datagrams.addAll(btnPressDatagramQueue);
        btnPressDatagramQueue.clear();

        return DatagramArrayEncoder.encode(datagrams);
    }

    //--------------------------------------------------------------------------------------------------
    // Misc
    //--------------------------------------------------------------------------------------------------

    private void updatePrefVals()
    {
        transmissionInterval = Integer.parseInt(globalPrefs.getString(PrefKeys.TX_INTERVAL, String.valueOf(DataConstants.DEFAULT_TX_INTERVAL_MS)));
        loadLastConfigOnStartup = globalPrefs.getBoolean("loadLastConfigOnStartup", true);
        enableByteDataType = globalPrefs.getBoolean("enableByteDatatype", false);
        enableButtonDataType = globalPrefs.getBoolean("enableButtonDatatype", false);
        deleteFieldOnLongPress = globalPrefs.getBoolean("deleteFieldOnLongPress", false);
        colorCoding = globalPrefs.getBoolean("colorCodeDatatypes", false);
        displayDatatype = globalPrefs.getBoolean("displayDatatype", false);
    }
}
