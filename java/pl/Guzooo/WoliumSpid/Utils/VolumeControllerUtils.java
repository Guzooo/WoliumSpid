package pl.Guzooo.WoliumSpid.Utils;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import pl.Guzooo.WoliumSpid.VolumeControllerData;
import pl.Guzooo.WoliumSpid.VolumeControllerService;

public class VolumeControllerUtils {

    public static PendingIntent getStopPendingIntent(Context context){
        Intent intent = getIntent(context);
        intent.putExtra(VolumeControllerService.EXTRA_ID, 0);
        return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void run(int profileId, Activity activity){
        if(VolumeControllerData.getCurrentId().getValue() == profileId) {
            stop(activity);
            return;
        }
        boolean canRun = checkPermissions(profileId, activity);
        if(!canRun)
            return;
        runVolumeController(profileId, activity);
    }

    public static void stop(Context context){
        Intent intent = getIntent(context);
        context.stopService(intent);
    }

    private static boolean checkPermissions(int profileId, Activity activity){
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, profileId);
            return false;//TODO: torche sie jeszcze pobawic
        }
        return true;
    }

    private static void runVolumeController(int profileId, Context context){
        Intent intent = getIntent(context);
        intent.putExtra(VolumeControllerService.EXTRA_ID, profileId);
        context.startService(intent);
    }

    private static Intent getIntent(Context context){
        return new Intent(context, VolumeControllerService.class);
    }
}
