package net.frogbots.ftcopmodetuner.ui.activity.hubtoolkit;

import android.support.v4.app.Fragment;

import net.frogbots.ftcopmodetunercommon.networking.datagram.hubtoolkit.HubToolkitReadDatagram;
import net.frogbots.ftcopmodetunercommon.networking.datagram.hubtoolkit.HubToolkitWriteDatagram;

public abstract class LynxControlFragment extends Fragment
{
    protected HubToolkitWriteDatagram writeDatagram;

    abstract void onDataUpdate(HubToolkitReadDatagram datagram);

    void setWriteDatagram(HubToolkitWriteDatagram writeDatagram)
    {
        this.writeDatagram = writeDatagram;
    }
}
