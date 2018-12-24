package net.frogbots.ftcopmodetuner.ui.activity.hubtoolkit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.frogbots.ftcopmodetuner.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * Use the {@link LynxModuleAnalogControlFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LynxModuleAnalogControlFragment extends Fragment
{
    AnalogPortFragment a0, a1, a2, a3;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LynxModuleAnalogControlFragment()
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
    public static LynxModuleAnalogControlFragment newInstance(String param1, String param2)
    {
        LynxModuleAnalogControlFragment fragment = new LynxModuleAnalogControlFragment();
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
        View v = inflater.inflate(R.layout.fragment_lynx_module_analog_control, container, false);

        a0 = (AnalogPortFragment) getChildFragmentManager().findFragmentById(R.id.analogPort0);
        a1 = (AnalogPortFragment) getChildFragmentManager().findFragmentById(R.id.analogPort1);
        a2 = (AnalogPortFragment) getChildFragmentManager().findFragmentById(R.id.analogPort2);
        a3 = (AnalogPortFragment) getChildFragmentManager().findFragmentById(R.id.analogPort3);

        a0.setNum(0);
        a1.setNum(1);
        a2.setNum(2);
        a3.setNum(3);

        return v;
    }
}
