package net.frogbots.ftcopmodetuner.ui.activity.hubtoolkit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.frogbots.ftcopmodetuner.R;
import net.frogbots.ftcopmodetunercommon.networking.datagram.hubtoolkit.HubToolkitReadDatagram;

public class LynxModuleServoControlFragment extends LynxControlFragment
{
    ServoFragment s0;
    ServoFragment s1;
    ServoFragment s2;
    ServoFragment s3;
    ServoFragment s4;
    ServoFragment s5;

    public LynxModuleServoControlFragment()
    {
        // Required empty public constructor
    }

    public static LynxModuleServoControlFragment newInstance()
    {
        LynxModuleServoControlFragment fragment = new LynxModuleServoControlFragment();
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
        View v = inflater.inflate(R.layout.fragment_lynx_module_servo_control, container, false);

        s0 = (ServoFragment) getChildFragmentManager().findFragmentById(R.id.servo0);
        s1 = (ServoFragment) getChildFragmentManager().findFragmentById(R.id.servo1);
        s2 = (ServoFragment) getChildFragmentManager().findFragmentById(R.id.servo2);
        s3 = (ServoFragment) getChildFragmentManager().findFragmentById(R.id.servo3);
        s4 = (ServoFragment) getChildFragmentManager().findFragmentById(R.id.servo4);
        s5 = (ServoFragment) getChildFragmentManager().findFragmentById(R.id.servo5);

        s0.setNum(0);
        s1.setNum(1);
        s2.setNum(2);
        s3.setNum(3);
        s4.setNum(4);
        s5.setNum(5);

        return v;
    }

    @Override
    void onDataUpdate(HubToolkitReadDatagram datagram)
    {

    }
}
