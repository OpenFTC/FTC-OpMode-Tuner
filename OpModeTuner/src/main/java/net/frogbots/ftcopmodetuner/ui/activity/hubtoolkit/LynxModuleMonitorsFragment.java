package net.frogbots.ftcopmodetuner.ui.activity.hubtoolkit;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import net.frogbots.ftcopmodetuner.R;
import net.frogbots.ftcopmodetunercommon.networking.datagram.hubtoolkit.HubToolkitDatagram;

import top.defaults.colorpicker.ColorPickerPopup;

public class LynxModuleMonitorsFragment extends LynxControlFragment
{
    TextView motor_0_currentDraw;
    boolean viewCreated;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lynx_module_monitors, container, false);

        motor_0_currentDraw = view.findViewById(R.id.motor_0_current_draw);

        viewCreated = true;

        return view;
    }

    @Override
    void onDataUpdate(HubToolkitDatagram datagram)
    {
        if(viewCreated)
        {
            motor_0_currentDraw.setText("Motor 0: " + datagram.motor0currentDraw + "A");
        }
    }
}
