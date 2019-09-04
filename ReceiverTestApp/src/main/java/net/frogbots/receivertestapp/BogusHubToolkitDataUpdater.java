package net.frogbots.receivertestapp;

import net.frogbots.ftcopmodetunercommon.networking.datagram.hubtoolkit.HubToolkitReadDatagram;

public class BogusHubToolkitDataUpdater
{
    private HubToolkitReadDatagram datagram;

    private MovingBogusValue mon12v = new MovingBogusValue(12000, 13000);
    private MovingBogusValue mon5v = new MovingBogusValue(4500, 5500);

    public BogusHubToolkitDataUpdater(HubToolkitReadDatagram datagram)
    {
        this.datagram = datagram;
    }

    public void update()
    {
        datagram.monitor_12v = (short) mon12v.next();
        datagram.monitor_5v = (short) mon5v.next();
    }
}
