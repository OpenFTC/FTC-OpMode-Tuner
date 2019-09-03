package net.frogbots.ftcopmodetuner.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import net.frogbots.ftcopmodetuner.R;
import net.frogbots.ftcopmodetuner.ui.activity.hubtoolkit.HubSelectionActivity;
import net.frogbots.ftcopmodetuner.ui.activity.hubtoolkit.LynxModuleControlActivity;

public class FunctionChooserActivity extends UdpConnectionActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_chooser);

        dsInstalledCheckAndShowDialog();
    }

    public void launchOpModeTuner(View v)
    {
        Intent intent = new Intent(this, OpModeTunerActivity.class);
        startActivity(intent);
    }

    public void launchHubToolkit(View v)
    {
        Intent intent = new Intent(this, HubSelectionActivity.class);
        startActivity(intent);
    }

    private void dsInstalledCheckAndShowDialog()
    {
        if(isFtcDriverStationInstalled(getPackageManager()))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.ds_app_detected_dialog_title);
            builder.setMessage(R.string.ds_app_detected_dialog_text);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {

                }
            });
            builder.setCancelable(false);
            builder.show();
        }
    }

    private boolean isFtcDriverStationInstalled(PackageManager packageManager)
    {
        try
        {
            packageManager.getPackageInfo(getString(R.string.driverstation_pkg_name), 0);
            return true;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            return false;
        }
    }
}
