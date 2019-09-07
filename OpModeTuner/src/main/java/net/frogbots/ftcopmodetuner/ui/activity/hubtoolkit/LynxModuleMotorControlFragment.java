package net.frogbots.ftcopmodetuner.ui.activity.hubtoolkit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.frogbots.ftcopmodetuner.R;
import net.frogbots.ftcopmodetunercommon.networking.datagram.hubtoolkit.HubToolkitReadDatagram;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * Use the {@link LynxModuleMotorControlFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LynxModuleMotorControlFragment extends LynxControlFragment
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private MotorFragment m0;
    private MotorFragment m1;
    private MotorFragment m2;
    private MotorFragment m3;

    public LynxModuleMotorControlFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LynxModuleMotorControlFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LynxModuleMotorControlFragment newInstance(String param1, String param2)
    {
        LynxModuleMotorControlFragment fragment = new LynxModuleMotorControlFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lynx_module_motor_control, container, false);

        m0 = (MotorFragment) getChildFragmentManager().findFragmentById(R.id.motor0);
        m1 = (MotorFragment) getChildFragmentManager().findFragmentById(R.id.motor1);
        m2 = (MotorFragment) getChildFragmentManager().findFragmentById(R.id.motor2);
        m3 = (MotorFragment) getChildFragmentManager().findFragmentById(R.id.motor3);

        return view;
    }

    @Override
    void onDataUpdate(HubToolkitReadDatagram datagram)
    {
        if(isVisible())
        {
            m0.setEncoder(datagram.motor0position_enc);
            m1.setEncoder(datagram.motor1position_enc);
            m2.setEncoder(datagram.motor2position_enc);
            m3.setEncoder(datagram.motor3position_enc);

            m0.setVelocity(datagram.motor0velocity_cps);
            m1.setVelocity(datagram.motor1velocity_cps);
            m2.setVelocity(datagram.motor2velocity_cps);
            m3.setVelocity(datagram.motor3velocity_cps);

            m0.setCurrentDraw(datagram.motor0currentDraw);
            m1.setCurrentDraw(datagram.motor1currentDraw);
            m2.setCurrentDraw(datagram.motor2currentDraw);
            m3.setCurrentDraw(datagram.motor3currentDraw);
        }
    }
}
