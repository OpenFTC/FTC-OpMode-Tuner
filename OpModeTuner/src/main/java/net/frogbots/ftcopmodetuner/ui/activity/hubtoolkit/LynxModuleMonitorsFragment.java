package net.frogbots.ftcopmodetuner.ui.activity.hubtoolkit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.frogbots.ftcopmodetuner.R;
import net.frogbots.ftcopmodetunercommon.networking.datagram.hubtoolkit.HubToolkitReadDatagram;

import java.text.DecimalFormat;

public class LynxModuleMonitorsFragment extends LynxControlFragment
{
    TextView motor_0_currentDraw;
    TextView motor_1_currentDraw;
    TextView motor_2_currentDraw;
    TextView motor_3_currentDraw;
    TextView gpioCurrentDraw;
    TextView i2cCurrentDraw;
    TextView totalCurrentDraw;
    TextView monitor_12v;
    TextView monitor_5v;

    DecimalFormat twoDecimalDigits = new DecimalFormat("0.00");

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
        motor_1_currentDraw = view.findViewById(R.id.motor_1_current_draw);
        motor_2_currentDraw = view.findViewById(R.id.motor_2_current_draw);
        motor_3_currentDraw = view.findViewById(R.id.motor_3_current_draw);
        gpioCurrentDraw = view.findViewById(R.id.gpio_current_draw);
        i2cCurrentDraw = view.findViewById(R.id.i2c_current_draw);
        totalCurrentDraw = view.findViewById(R.id.total_current_draw);
        monitor_12v = view.findViewById(R.id.monitor_12v);
        monitor_5v = view.findViewById(R.id.monitor_5v);

        return view;
    }

    @Override
    void onDataUpdate(HubToolkitReadDatagram datagram)
    {
        if(isVisible())
        {
            motor_0_currentDraw.setText("Motor 0: " + twoDecimalDigits.format(datagram.motor0currentDraw/1000f) + "A");
            motor_1_currentDraw.setText("Motor 1: " + twoDecimalDigits.format(datagram.motor1currentDraw/1000f) + "A");
            motor_2_currentDraw.setText("Motor 2: " + twoDecimalDigits.format(datagram.motor2currentDraw/1000f) + "A");
            motor_3_currentDraw.setText("Motor 3: " + twoDecimalDigits.format(datagram.motor3currentDraw/1000f) + "A");
            totalCurrentDraw.setText("Total: " + twoDecimalDigits.format(datagram.totalCurrentDraw/1000f) + "A");
            gpioCurrentDraw.setText("GPIO: " + datagram.gpioCurrentDraw + "mA");
            i2cCurrentDraw.setText("I2C: " + datagram.i2cCurrentDraw + "mA");
            monitor_12v.setText("12v monitor: " + twoDecimalDigits.format(datagram.monitor_12v/1000f) + "v");
            monitor_5v.setText("5v monitor: " + twoDecimalDigits.format(datagram.monitor_5v/1000f) + "v");

        }
    }
}
