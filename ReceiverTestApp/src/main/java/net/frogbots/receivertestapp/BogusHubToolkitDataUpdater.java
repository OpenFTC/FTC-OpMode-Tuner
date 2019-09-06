package net.frogbots.receivertestapp;

import net.frogbots.ftcopmodetunercommon.networking.datagram.hubtoolkit.HubToolkitReadDatagram;

public class BogusHubToolkitDataUpdater
{
    private HubToolkitReadDatagram datagram;

    private MovingBogusValue mon12v = new MovingBogusValue(12000, 13000);
    private MovingBogusValue mon5v = new MovingBogusValue(4500, 5500);

    private MovingBogusValue a0 = new MovingBogusValue(0, 500);
    private MovingBogusValue a1 = new MovingBogusValue(1000, 1500);
    private MovingBogusValue a2 = new MovingBogusValue(2000, 2500);
    private MovingBogusValue a3 = new MovingBogusValue(3000, 3300);

    public BogusHubToolkitDataUpdater(HubToolkitReadDatagram datagram)
    {
        this.datagram = datagram;
    }

    public void update()
    {
        datagram.monitor_12v = (short) mon12v.next();
        datagram.monitor_5v = (short) mon5v.next();

        datagram.analog0_mV = (short) a0.next();
        datagram.analog1_mV = (short) a1.next();
        datagram.analog2_mV = (short) a2.next();
        datagram.analog3_mV = (short) a3.next();
    }
}
