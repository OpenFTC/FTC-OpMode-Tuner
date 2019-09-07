package net.frogbots.ftcopmodetuner.ui.activity.hubtoolkit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import net.frogbots.ftcopmodetuner.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * Use the {@link LynxModuleMotorControlFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MotorFragment extends Fragment
{
    private Spinner runModesSpinner;
    private TextView encoder;
    private TextView velocity;
    private TextView current;

    public MotorFragment()
    {
        // Required empty public constructor
    }

    public static MotorFragment newInstance()
    {
        MotorFragment fragment = new MotorFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_motor, container, false);

        runModesSpinner = view.findViewById(R.id.motorRunModeSpinner);
        ArrayAdapter<CharSequence> modesAdapter = ArrayAdapter.createFromResource(getContext(), R.array.motorRunModes, android.R.layout.simple_spinner_item);
        modesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        runModesSpinner.setAdapter(modesAdapter);

        encoder = view.findViewById(R.id.encoder);
        velocity = view.findViewById(R.id.velocity);
        current = view.findViewById(R.id.current);

        return view;
    }

    public void setEncoder(int enc)
    {
        encoder.setText("Encoder: " + enc);
    }

    public void setVelocity(int vel)
    {
        velocity.setText("Velocity: " + vel);
    }

    public void setCurrentDraw(int ma)
    {
        current.setText("Current: " + ma/1000f + "A");
    }
}
