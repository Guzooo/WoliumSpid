package pl.Guzooo.WoliumSpid.Utils;

import android.content.Context;
import android.content.Intent;

import pl.Guzooo.WoliumSpid.VolumeControllerService;

public class VolumeControllerUtils {

    public static void run(int profileId, Context context){
        //TODO: jeżeli id jest takie samo jak VolumeControllerData to zatrzymaj Service;
        boolean canRun = checkPermissions();
        if(!canRun)
            return;
        runVolumeController(profileId, context);
    }

    public static void stop(Context context){
        Intent intent = getIntent(context);
        context.stopService(intent);
    }

    private static boolean checkPermissions(){
        //TODO: sprawdz czy powiadomienia wlaczone;
        // też lokalizacja czy wlaczona i czy mozna uzywac
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
