package net.frogbots.ftcopmodetuner.ui.activity.hubtoolkit;

import android.support.v4.app.Fragment;

import net.frogbots.ftcopmodetunercommon.networking.datagram.hubtoolkit.HubToolkitDatagram;

public abstract class LynxControlFragment extends Fragment
{
    abstract void onDataUpdate(HubToolkitDatagram datagram);
}
