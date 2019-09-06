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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import net.frogbots.ftcopmodetuner.R;
import net.frogbots.ftcopmodetuner.ui.field.util.FloatingPointSignedSeekBar;

public class ServoFragment extends Fragment implements FloatingPointSignedSeekBar.OnFloatingPointSeekBarChangeListener
{
    private FloatingPointSignedSeekBar floatingPointSignedSeekBar;
    private TextView name;
    private TextView posTextView;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_servo, container, true);

        name = view.findViewById(R.id.name);
        posTextView = view.findViewById(R.id.posTextView);

        floatingPointSignedSeekBar = new FloatingPointSignedSeekBar((SeekBar) view.findViewById(R.id.servoPos), 0, 1);
        floatingPointSignedSeekBar.setOnSeekBarChangeListener(this);

        return view;
    }

    public void setNum(int num)
    {
        name.setText("Servo " + num);
    }

    @Override
    public void onProgressChanged(FloatingPointSignedSeekBar seekBar, double progress, boolean fromUser)
    {
        posTextView.setText("Position: " + progress);
    }
}
