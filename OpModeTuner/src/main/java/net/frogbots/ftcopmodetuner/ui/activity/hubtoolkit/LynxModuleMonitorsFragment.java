package net.frogbots.ftcopmodetuner.ui.activity.hubtoolkit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.frogbots.ftcopmodetuner.R;
import net.frogbots.ftcopmodetunercommon.networking.datagram.hubtoolkit.HubToolkitReadDatagram;

public class LynxModuleMonitorsFragment extends LynxControlFragment
{
    TextView motor_0_currentDraw;
    TextView monitor_12v;
    TextView monitor_5v;
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
        monitor_12v = view.findViewById(R.id.monitor_12v);
        monitor_5v = view.findViewById(R.id.monitor_5v);

        viewCreated = true;

        return view;
    }

    @Override
    void onDataUpdate(HubToolkitReadDatagram datagram)
    {
        if(viewCreated)
        {
            motor_0_currentDraw.setText("Motor 0: " + datagram.motor0currentDraw + "A");
            monitor_12v.setText("12v monitor: " + datagram.monitor_12v/1000f + "v");
            monitor_5v.setText("5v monitor: " + datagram.monitor_5v/1000f + "v");
        }
    }
}
