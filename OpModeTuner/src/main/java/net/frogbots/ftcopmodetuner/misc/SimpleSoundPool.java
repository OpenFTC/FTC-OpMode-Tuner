package net.frogbots.ftcopmodetuner.misc;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.annotation.RawRes;

public class SimpleSoundPool extends SoundPool
{
    private Context context;

    public SimpleSoundPool(Context context)
    {
        super(5, AudioManager.STREAM_MUSIC, 0);
        this.context = context;
    }

    public int load(@RawRes int id)
    {
        return super.load(context, id, 1);
    }

    public void play(int id)
    {
        super.play(id, 1, 1, 0, 0, 1);
    }
}
