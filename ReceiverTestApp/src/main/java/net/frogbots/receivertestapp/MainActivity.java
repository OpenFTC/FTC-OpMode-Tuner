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

package net.frogbots.receivertestapp;

import android.app.Activity;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.frogbots.ftcopmodetunercommon.misc.DataConstants;
import net.frogbots.ftcopmodetunercommon.networking.datagram.Datagram;
import net.frogbots.ftcopmodetunercommon.networking.datagram.hubtoolkit.HubToolkitReadDatagram;
import net.frogbots.ftcopmodetunercommon.networking.datagram.hubtoolkit.HubToolkitWriteDatagram;
import net.frogbots.ftcopmodetunercommon.networking.receiver.FtcOpModeTunerReceiver;
import net.frogbots.ftcopmodetunercommon.networking.receiver.FtcOpModeTunerReceiverInterface;
import net.frogbots.ftcopmodetunercommon.networking.udp.CommandList;
import net.frogbots.ftcopmodetunercommon.networking.udp.HubToolkitReadDataMsg;
import net.frogbots.ftcopmodetunercommon.networking.udp.HubToolkitWriteDataMsg;
import net.frogbots.ftcopmodetunercommon.networking.udp.NetworkCommand;

import java.util.Locale;

public class MainActivity extends Activity implements FtcOpModeTunerReceiverInterface
{
    volatile FtcOpModeTunerReceiver receiver;
    TextView textView;
    TextView myIp;
    TextView connected;
    Thread thread;
    WifiManager wm;
    LinearLayout layout;
    HubToolkitReadDatagram hubToolkitReadDatagram = new HubToolkitReadDatagram();
    HubToolkitWriteDatagram hubToolkitWriteDatagram = new HubToolkitWriteDatagram();
    HubToolkitStreamer hubToolkitStreamer;
    BogusHubToolkitDataUpdater bogusHubToolkitDataUpdater = new BogusHubToolkitDataUpdater(hubToolkitReadDatagram);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = findViewById(R.id.layout);

        textView = findViewById(R.id.textView);
        connected = findViewById(R.id.connection);
        myIp = findViewById(R.id.myIp);

        wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        receiver = new FtcOpModeTunerReceiver(this, this);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        receiver.begin(DataConstants.DEFAULT_PORT);
        backgroundThread();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        thread.interrupt();
        receiver.close();
    }

    void backgroundThread()
    {
        thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (!Thread.currentThread().isInterrupted())
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            updateUi();
                        }
                    });

                    try
                    {
                        Thread.sleep(25);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });

        thread.start();
    }

    private void updateUi()
    {
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        myIp.setText("This device's IP is: " + ip);

        StringBuilder text = new StringBuilder();

        for(Datagram d : receiver.getAll())
        {
            text.append(String.format(Locale.US, "Tag: %s Data: %s\n", d.getTag(), d.getStringValue()));
        }

        textView.setText(text.toString());

        if(receiver.isConnected())
        {
            connected.setText(String.format("Connected to: %s", receiver.getTunerAppAddr()));
        }
        else
        {
            connected.setText("NOT Connected");
        }
    }

    @Override
    public void onButtonPressEvent(final String tag)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                Toast.makeText(MainActivity.this, tag,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCommand(NetworkCommand command)
    {
        if(command.getName().equals(CommandList.QUERY_LIST_OF_LYNX_MODULES.toString()))
        {
            NetworkCommand resp = new NetworkCommand(CommandList.QUERY_LIST_OF_LYNX_MODULES_RESP.toString(), "Expansion Hub 2;Expansion Hub 3");
            receiver.sendMsg(resp);
        }
        else if(command.getName().equals(CommandList.START_HUBTOOLKIT_STREAM.toString()))
        {
            String module = command.getExtra();

            if(hubToolkitStreamer == null)
            {
                hubToolkitStreamer = new HubToolkitStreamer();
                hubToolkitStreamer.start();
            }
        }
        else if(command.getName().equals(CommandList.STOP_HUBTOOLKIT_STREAM.toString()))
        {
            if(hubToolkitStreamer != null)
            {
                hubToolkitStreamer.interrupt();
                hubToolkitStreamer = null;
            }
        }
    }

    @Override
    public void onHubToolkitWriteData(HubToolkitWriteDataMsg hubToolkitWriteDataMsg)
    {
        hubToolkitWriteDatagram.fromByteArray(hubToolkitWriteDataMsg.getData());

        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                layout.setBackgroundColor(hubToolkitWriteDatagram.ledRgb);
            }
        });
    }

    class HubToolkitStreamer extends Thread
    {
        @Override
        public void run()
        {
            while (!Thread.currentThread().isInterrupted())
            {
                bogusHubToolkitDataUpdater.update();

                HubToolkitReadDataMsg hubToolkitReadDataMsg = new HubToolkitReadDataMsg();
                hubToolkitReadDataMsg.setData(hubToolkitReadDatagram.encode());
                receiver.sendMsg(hubToolkitReadDataMsg);

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
