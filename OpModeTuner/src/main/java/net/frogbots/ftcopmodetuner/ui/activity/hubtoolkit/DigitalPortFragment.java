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
import android.widget.TextView;
import android.widget.ToggleButton;

import net.frogbots.ftcopmodetuner.R;

public class DigitalPortFragment extends Fragment implements View.OnClickListener
{
    Mode mode;
    TextView num;
    Button inModeBtn;
    Button outModeBtn;
    TextView rwTxtView;
    ToggleButton highLowToggleButton;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_digital_port, container, true);

        num = view.findViewById(R.id.num);
        inModeBtn = view.findViewById(R.id.inModeBtn);
        outModeBtn = view.findViewById(R.id.outModeBtn);
        rwTxtView = view.findViewById(R.id.rwTxtView);
        highLowToggleButton = view.findViewById(R.id.highLowToggleBtn);

        inModeBtn.setOnClickListener(this);
        outModeBtn.setOnClickListener(this);
        highLowToggleButton.setOnClickListener(this);

        setMode(Mode.INPUT);

        return view;
    }

    public enum Mode
    {
        INPUT,
        OUTPUT
    }

    public void setMode(Mode mode)
    {
        this.mode = mode;

        if(mode == Mode.INPUT)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                rwTxtView.setText("READ:");

                inModeBtn.setTextColor(getResources().getColor(R.color.white));
                inModeBtn.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorPrimary));

                outModeBtn.setTextColor(Color.BLACK);
                outModeBtn.setBackgroundTintList(null);

                highLowToggleButton.setChecked(false);
                highLowToggleButton.setEnabled(false);
            }
        }
        else if(mode == Mode.OUTPUT)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                rwTxtView.setText("WRITE:");

                inModeBtn.setTextColor(Color.BLACK);
                inModeBtn.setBackgroundTintList(null);

                outModeBtn.setTextColor(getResources().getColor(R.color.white));
                outModeBtn.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorPrimary));

                highLowToggleButton.setEnabled(true);
            }
        }
    }

    public void setNum(int num)
    {
        this.num.setText("#" + num);
    }

    @Override
    public void onClick(View v)
    {
        if(v == inModeBtn)
        {
            setMode(Mode.INPUT);
        }
        else if(v == outModeBtn)
        {
            setMode(Mode.OUTPUT);
        }
        else if(v == highLowToggleButton)
        {

        }
    }
}
