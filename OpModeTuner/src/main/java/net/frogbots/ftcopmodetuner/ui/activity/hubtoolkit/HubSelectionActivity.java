package net.frogbots.ftcopmodetuner.ui.activity.hubtoolkit;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import net.frogbots.ftcopmodetuner.R;
import net.frogbots.ftcopmodetuner.ui.activity.UdpConnectionActivity;
import net.frogbots.ftcopmodetunercommon.networking.udp.Command;
import net.frogbots.ftcopmodetunercommon.networking.udp.NetworkCommand;

public class HubSelectionActivity extends UdpConnectionActivity
{
    ListView hubListView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub_selection);

        getSupportActionBar().setTitle("REV Hub Toolkit");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.hub_toolkit_actionbar_color)));
        /*
         * Set the status bar color to colorPrimaryDark
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.hub_toolkit_actionbar_color));
        }

        hubListView = findViewById(R.id.hubListView);

        NetworkCommand networkCommand = new NetworkCommand(Command.QUERY_LIST_OF_LYNX_MODULES.toString());

        networkCommand.setListener(new NetworkCommand.AckOrNackListener()
        {
            @Override
            public void onAck()
            {
                
            }

            @Override
            public void onNack()
            {

            }
        });

        networkingManager.sendMsg(networkCommand);
    }
}
