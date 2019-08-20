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

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.frogbots.ftcopmodetuner.R;
import net.frogbots.ftcopmodetuner.network.NetworkEventsListener;
import net.frogbots.ftcopmodetuner.network.NetworkedApplication;
import net.frogbots.ftcopmodetuner.network.NetworkingManager;
import net.frogbots.ftcopmodetuner.prefs.GlobalPrefs;
import net.frogbots.ftcopmodetunercommon.networking.udp.ConnectionStatus;

import java.util.Locale;

public abstract class UdpConnectionActivity extends AppCompatActivity implements NetworkEventsListener
{
    private LinearLayout rootLayout;
    private LinearLayout connectionStatusAreaLayout;
    protected NetworkingManager networkingManager;
    private TextView connectionStatus;
    private int port;
    private String addr;
    private GlobalPrefs globalPrefs;
    private WifiManager.WifiLock wifiLock;
    private NetworkedApplication networkedApplication;

    //--------------------------------------------------------------------------------------------------
    // Android Lifecycle
    //--------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_udp_connection);

        networkedApplication = (NetworkedApplication) getApplication();

        globalPrefs = GlobalPrefs.getInstance();

        showIntroIfRequested();

        rootLayout = findViewById(R.id.rootLayout);
        connectionStatusAreaLayout = findViewById(R.id.connectionStatusLayout);
        connectionStatus = findViewById(R.id.connectionStatus);

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiLock = wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL_HIGH_PERF, "");

        networkingManager = NetworkingManager.getInstance();
        updatePrefVals();
    }

    public void addThingBelowConnectionStatusView(@LayoutRes int id)
    {
        getLayoutInflater().inflate(id, connectionStatusAreaLayout);
    }

    @Override
    public void setContentView(@LayoutRes int id)
    {
        getLayoutInflater().inflate(id, rootLayout);
    }

    @Override
    public void onPause()
    {
        super.onPause();

        wifiLock.release();
        networkingManager.removeListener(this);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        updatePrefVals();
        wifiLock.acquire();
        networkingManager.registerListener(this);
        updateUiConnectionState(networkingManager.getConnectionStatus());
    }

    @Override
    public void onSaveInstanceState(Bundle bundle)
    {
        super.onSaveInstanceState(bundle);
        connectionStatus.setText(Html.fromHtml(""));
    }


    //--------------------------------------------------------------------------------------------------
    // UI Stuff
    //--------------------------------------------------------------------------------------------------

    protected void updateUiConnectionState(ConnectionStatus status)
    {
        /*
         * Connected
         */
        if(status == ConnectionStatus.CONNECTED)
        {
            connectionStatus.setText(String.format(Locale.US, getString(R.string.connection_status_connected), addr, port));
            connectionStatus.setBackgroundColor(getResources().getColor(R.color.connected));
        }

        /*
         * Not connected
         */
        else if (status == ConnectionStatus.NOT_CONNECTED
                || status == ConnectionStatus.INITIALIZING)
        {
            connectionStatus.setText(String.format(Locale.US, getString(R.string.connection_status_disconnected), addr, port));
            connectionStatus.setBackgroundColor(getResources().getColor(R.color.disconnected));
        }

        /*
         * Connection refused
         */
        else if(status == ConnectionStatus.CONNECTION_REFUSED)
        {
            connectionStatus.setText(R.string.connection_refused);
            connectionStatus.setBackgroundColor(getResources().getColor(R.color.byte_color));
        }

        else if(status == ConnectionStatus.WIFI_RADIO_OFF_OR_NOT_CONNECTED_TO_INFRASTRUCTURE_NETWORK)
        {
            connectionStatus.setBackgroundColor(getResources().getColor(R.color.wifi_off));
            connectionStatus.setText(R.string.wifi_off_or_not_connected);
        }
    }

    private void showIntroIfRequested()
    {
        if(globalPrefs.getBoolean(R.string.prefkey_showIntro, true))
        {
            globalPrefs.edit().putBoolean(R.string.prefkey_showIntro, false).apply();
            Intent intent = new Intent(this, AppIntroActivity.class);
            startActivity(intent);
        }
    }

    //--------------------------------------------------------------------------------------------------
    // Networking
    //--------------------------------------------------------------------------------------------------

    public void onNetworkStateUpdateFromBackgroundThread(final ConnectionStatus status, long eventTime)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                updateUiConnectionState(status);
            }
        });
    }

    //--------------------------------------------------------------------------------------------------
    // Misc
    //--------------------------------------------------------------------------------------------------

    private void updatePrefVals()
    {
        addr = networkedApplication.getAddr();
        port = networkedApplication.getPort();
    }
}
