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

package net.frogbots.ftcopmodetunercommon.opmode;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerNotifier;

import net.frogbots.ftcopmodetunercommon.R;
import net.frogbots.ftcopmodetunercommon.misc.DataConstants;
import net.frogbots.ftcopmodetunercommon.networking.receiver.FtcOpModeTunerReceiver;
import net.frogbots.ftcopmodetunercommon.networking.receiver.FtcOpModeTunerReceiverInterface;

import org.firstinspires.ftc.robotcore.internal.opmode.OpModeManagerImpl;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The class that does the bulk of the actual work for TunableOpMode and
 * TunableOpModeCommon. Needed because Java doesn't support multiple inheritance
 */

class TunableOpModeCommon implements FtcOpModeTunerReceiverInterface
{
    FtcOpModeTunerReceiver receiver;
    private TextView tunerConnectionStatusTxtView;
    private Runnable connectionStatusUpdaterRunnable;
    private ScheduledExecutorService connectionStatusUpdaterTimer;
    private Activity activity;
    private RelativeLayout relativeLayout;
    private RelativeLayout.LayoutParams tunerConnectionStatusParams;
    private RelativeLayout.LayoutParams networkStatusParams;
    protected AppUtil appUtil = AppUtil.getInstance();
    protected OpModeManagerImpl opModeManager = null;
    protected OpModeNotifications opModeNotifications = new OpModeNotifications();
    private TunableOpModeInterface opMode;

    TunableOpModeCommon(TunableOpModeInterface opMode)
    {
        this.opMode = opMode;
        activity = appUtil.getActivity();
        opModeManager = OpModeManagerImpl.getOpModeManagerOfActivity(activity);
        opModeManager.registerListener(opModeNotifications);
    }

    void init()
    {
        receiver = new FtcOpModeTunerReceiver(activity, this);
        receiver.begin(getPort());

        tunerConnectionStatusTxtView = new TextView(activity);
        tunerConnectionStatusTxtView.setId(View.generateViewId());
        tunerConnectionStatusTxtView.setTextColor(activity.getResources().getColor(R.color.tunerStatusBlue));
        tunerConnectionStatusTxtView.setTypeface(Typeface.DEFAULT_BOLD);
        int relativeLayoutId = activity.getResources().getIdentifier("RelativeLayout", "id", activity.getPackageName());
        final int networkStatusTxtViewId = activity.getResources().getIdentifier("textNetworkConnectionStatus", "id", activity.getPackageName());

        relativeLayout = activity.findViewById(relativeLayoutId);
        tunerConnectionStatusParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        networkStatusParams = (RelativeLayout.LayoutParams) activity.findViewById(networkStatusTxtViewId).getLayoutParams();
        networkStatusParams.addRule(RelativeLayout.BELOW, tunerConnectionStatusTxtView.getId());

        activity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                relativeLayout.addView(tunerConnectionStatusTxtView, tunerConnectionStatusParams);
                activity.findViewById(networkStatusTxtViewId).setLayoutParams(networkStatusParams);
            }
        });

        setupUpdateConnectionStatusTask();
    }

    void stop()
    {
        connectionStatusUpdaterTimer.shutdown();
        receiver.close();

        activity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                relativeLayout.removeView(tunerConnectionStatusTxtView);
            }
        });
    }

    public int getPort()
    {
        return DataConstants.DEFAULT_PORT;
    }

    public void hideTunerConnectionStatus()
    {
        activity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                tunerConnectionStatusTxtView.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void showTunerConnectionStatus()
    {
        activity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                tunerConnectionStatusTxtView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setupUpdateConnectionStatusTask()
    {
        connectionStatusUpdaterRunnable = new Runnable()
        {
            @Override
            public void run()
            {
                if(receiver.isConnected())
                {
                    activity.runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            tunerConnectionStatusTxtView.setText("Tuner: connected at " + receiver.getTunerAppAddr());
                        }
                    });
                }
                else
                {
                    activity.runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            tunerConnectionStatusTxtView.setText("Tuner: disconnected");
                        }
                    });
                }
            }
        };

        connectionStatusUpdaterTimer = Executors.newSingleThreadScheduledExecutor();
        connectionStatusUpdaterTimer.scheduleAtFixedRate(connectionStatusUpdaterRunnable, 0, 100, TimeUnit.MILLISECONDS);
    }

    @Override
    public void onButtonPressEvent(String tag)
    {
        opMode.onButtonPressEvent(tag);
    }

    protected class OpModeNotifications implements OpModeManagerNotifier.Notifications
    {
        @Override
        public void onOpModePreInit(OpMode opMode)
        {
            init();
        }

        @Override
        public void onOpModePreStart(OpMode opMode)
        {
        }

        @Override
        public void onOpModePostStop(OpMode opMode)
        {
            /** We automatically shut down after the opmode (in which we are started) stops.  */
            stop();
            if (opModeManager != null)
            {
                opModeManager.unregisterListener(opModeNotifications);
            }
        }
    }
}
