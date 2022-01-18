package pl.Guzooo.WoliumSpid.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.Toast;

public class PermissionsRequestUtils {

    public static void localizationOnRunProfile(int profileID, int[] grantResults, Activity activity){
        boolean permissionGranted = false;
        for (int grantResult : grantResults)
            if (grantResult == PackageManager.PERMISSION_GRANTED) {
                permissionGranted = true;
                break;
            }
        if (permissionGranted)
            VolumeControllerUtils.run(profileID, activity);
        else
            Toast.makeText(activity, "Potrzebne uprawnienia lokalizacji", Toast.LENGTH_SHORT).show();
    }
}
