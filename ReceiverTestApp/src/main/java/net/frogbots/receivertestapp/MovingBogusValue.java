package net.frogbots.receivertestapp;

public class MovingBogusValue
{
    private int min, max;
    private boolean up = true;
    private int i;

    public MovingBogusValue(int min, int max)
    {
        this.min = min;
        this.max = max;

        i = min;
    }

    public int next()
    {
        if(up)
        {
            i++;
        }
        else
        {
            i--;
        }

        if(i <= min || i >= max)
        {
            up = !up;
        }

        return i;
    }
}
