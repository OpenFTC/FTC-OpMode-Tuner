package net.frogbots.receivertestapp;

import net.frogbots.ftcopmodetunercommon.networking.datagram.hubtoolkit.HubToolkitReadDatagram;

import java.util.Random;

public class BogusHubToolkitDataUpdater
{
    private HubToolkitReadDatagram datagram;

    private MovingBogusValue mon12v = new MovingBogusValue(12000, 13000);
    private MovingBogusValue mon5v = new MovingBogusValue(4500, 5500);

    private MovingBogusValue a0 = new MovingBogusValue(0, 500);
    private MovingBogusValue a1 = new MovingBogusValue(1000, 1500);
    private MovingBogusValue a2 = new MovingBogusValue(2000, 2500);
    private MovingBogusValue a3 = new MovingBogusValue(3000, 3300);

    private MovingBogusValue m0_current = new MovingBogusValue(0, 1000);
    private MovingBogusValue m1_current = new MovingBogusValue(1000, 2000);
    private MovingBogusValue m2_current = new MovingBogusValue(2000, 3000);
    private MovingBogusValue m3_current = new MovingBogusValue(3000, 4000);
    private MovingBogusValue gpio_current = new MovingBogusValue(0, 250);
    private MovingBogusValue i2c_current = new MovingBogusValue(250, 500);
    private MovingBogusValue total_current = new MovingBogusValue(5000, 6000);

    private MovingBogusValue m0_enc = new MovingBogusValue(0, 1000);
    private MovingBogusValue m1_enc = new MovingBogusValue(1000, 2000);
    private MovingBogusValue m2_enc = new MovingBogusValue(2000, 3000);
    private MovingBogusValue m3_enc = new MovingBogusValue(3000, 4000);

    private MovingBogusValue m0_vel = new MovingBogusValue(0, 100);
    private MovingBogusValue m1_vel = new MovingBogusValue(100, 200);
    private MovingBogusValue m2_vel = new MovingBogusValue(200, 300);
    private MovingBogusValue m3_vel = new MovingBogusValue(300, 400);

    private Random random = new Random();

    long lastDigitalUpdateTime;

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

        if(System.currentTimeMillis() - lastDigitalUpdateTime > 1000)
        {
            datagram.digitalInputs = (byte) random.nextInt(256);
            lastDigitalUpdateTime = System.currentTimeMillis();
        }

        datagram.motor0currentDraw = (short) m0_current.next();
        datagram.motor1currentDraw = (short) m1_current.next();
        datagram.motor2currentDraw = (short) m2_current.next();
        datagram.motor3currentDraw = (short) m3_current.next();

        datagram.gpioCurrentDraw = (short) gpio_current.next();
        datagram.i2cCurrentDraw = (short) i2c_current.next();
        datagram.totalCurrentDraw = (short) total_current.next();

        datagram.motor0position_enc = m0_enc.next();
        datagram.motor1position_enc = m1_enc.next();
        datagram.motor2position_enc = m2_enc.next();
        datagram.motor3position_enc = m3_enc.next();

        datagram.motor0velocity_cps = (short) m0_vel.next();
        datagram.motor1velocity_cps = (short) m1_vel.next();
        datagram.motor2velocity_cps = (short) m2_vel.next();
        datagram.motor3velocity_cps = (short) m3_vel.next();
    }
}
