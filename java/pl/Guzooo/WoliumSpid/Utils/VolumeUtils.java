package pl.Guzooo.WoliumSpid.Utils;

import android.content.Context;
import android.media.AudioManager;

public class VolumeUtils {

    private static int volumeMax = 0;

    public static int getVolumeMax(Context context) {
        if(volumeMax == 0){
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            volumeMax = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        }
        return volumeMax;
    }
}
