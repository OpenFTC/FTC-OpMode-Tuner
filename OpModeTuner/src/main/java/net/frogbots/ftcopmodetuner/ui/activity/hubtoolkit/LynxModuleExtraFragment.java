package net.frogbots.ftcopmodetuner.ui.activity.hubtoolkit;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.frogbots.ftcopmodetuner.R;
import net.frogbots.ftcopmodetunercommon.networking.datagram.hubtoolkit.HubToolkitReadDatagram;

import top.defaults.colorpicker.ColorPickerPopup;

public class LynxModuleExtraFragment extends LynxControlFragment
{
    Button setLedColor;
    View statusLedColorView;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lynx_module_extra, container, false);

        statusLedColorView = view.findViewById(R.id.statusLedColorView);
        statusLedColorView.setBackgroundColor(Color.MAGENTA);

        setLedColor = view.findViewById(R.id.setExpansionHubLedColor);
        setLedColor.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                new ColorPickerPopup.Builder(getActivity())
                        .initialColor(Color.MAGENTA) // Set initial color
                        .enableBrightness(true) // Enable brightness slider or not
                        .enableAlpha(false) // Enable alpha slider or not
                        .okTitle("Choose")
                        .cancelTitle("Cancel")
                        .showIndicator(true)
                        .showValue(false)
                        .build()
                        .show(new ColorPickerPopup.ColorPickerObserver()
                        {
                            @Override
                            public void onColorPicked(int color)
                            {
                                statusLedColorView.setBackgroundColor(color);
                                writeDatagram.ledRgb = color;
                            }

                            @Override
                            public void onColor(int color, boolean fromUser)
                            {

                            }
                        });
            }
        });

        return view;
    }

    @Override
    void onDataUpdate(HubToolkitReadDatagram datagram)
    {

    }
}
