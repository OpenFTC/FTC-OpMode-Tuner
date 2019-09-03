package net.frogbots.ftcopmodetuner.ui.activity.hubtoolkit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.frogbots.ftcopmodetuner.R;
import net.frogbots.ftcopmodetunercommon.networking.datagram.hubtoolkit.HubToolkitDatagram;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * Use the {@link LynxModuleDigitalControlFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LynxModuleDigitalControlFragment extends LynxControlFragment
{
    DigitalPortFragment p0, p1, p2, p3, p4, p5, p6, p7;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LynxModuleDigitalControlFragment()
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
    public static LynxModuleDigitalControlFragment newInstance(String param1, String param2)
    {
        LynxModuleDigitalControlFragment fragment = new LynxModuleDigitalControlFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_lynx_module_digital_control, container, false);

        p0 = (DigitalPortFragment) getChildFragmentManager().findFragmentById(R.id.digitalPort0);
        p1 = (DigitalPortFragment) getChildFragmentManager().findFragmentById(R.id.digitalPort1);
        p2 = (DigitalPortFragment) getChildFragmentManager().findFragmentById(R.id.digitalPort2);
        p3 = (DigitalPortFragment) getChildFragmentManager().findFragmentById(R.id.digitalPort3);
        p4 = (DigitalPortFragment) getChildFragmentManager().findFragmentById(R.id.digitalPort4);
        p5 = (DigitalPortFragment) getChildFragmentManager().findFragmentById(R.id.digitalPort5);
        p6 = (DigitalPortFragment) getChildFragmentManager().findFragmentById(R.id.digitalPort6);
        p7 = (DigitalPortFragment) getChildFragmentManager().findFragmentById(R.id.digitalPort7);

        p0.setNum(0);
        p1.setNum(1);
        p2.setNum(2);
        p3.setNum(3);
        p4.setNum(4);
        p5.setNum(5);
        p6.setNum(6);
        p7.setNum(7);

        return v;
    }

    @Override
    void onDataUpdate(HubToolkitDatagram datagram) {

    }
}
