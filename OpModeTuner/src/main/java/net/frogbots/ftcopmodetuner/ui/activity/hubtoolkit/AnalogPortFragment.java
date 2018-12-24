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

public class AnalogPortFragment extends Fragment
{
    ProgressBar graphicalVoltage;
    TextView rawVoltage;
    TextView javaVoltage;
    TextView num;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_analog_port, container, true);

        graphicalVoltage = view.findViewById(R.id.graphicalVoltage);
        rawVoltage = view.findViewById(R.id.rawVoltage);
        javaVoltage = view.findViewById(R.id.javaVoltage);
        num = view.findViewById(R.id.num);

        return view;
    }

    public void setNum(int num)
    {
        this.num.setText("Analog " + num);
    }
}
