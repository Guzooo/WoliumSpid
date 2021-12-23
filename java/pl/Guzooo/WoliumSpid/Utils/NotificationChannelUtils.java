package pl.Guzooo.WoliumSpid.Utils;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.annotation.StringDef;
import androidx.core.app.NotificationManagerCompat;

import pl.Guzooo.Base.Utils.FragmentationUtils;

public class NotificationChannelUtils {

    @StringDef({
        VOLUME_CONTROLLER_CHANNEL_ID
    })
    private @interface Channels {}
    public static final String VOLUME_CONTROLLER_CHANNEL_ID = "volumecontrollerchannelid";

    @SuppressLint("NewApi")
    public static void createNotificationChannels(Context context){
        if(!FragmentationUtils.isMinimumOreo())
            return;
        createVolumeControllerChannel(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean isChannelActive(@Channels String channelId, Context context){
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = manager.getNotificationChannel(channelId);
        if(channel.getImportance() == NotificationManagerCompat.IMPORTANCE_NONE)
            return false;
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void openChannelSettings(@Channels String channelId, Context context){
        Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
        intent.putExtra(Settings.EXTRA_CHANNEL_ID, channelId);
        context.startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void createVolumeControllerChannel(Context context){
        String name = "a"; //TODO: string
        String description = "b"; //TODO: string
        int importance = NotificationManagerCompat.IMPORTANCE_LOW;
        createChannel(VOLUME_CONTROLLER_CHANNEL_ID, name, description, importance, context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void createChannel(String id, String name, String description, int importance, Context context){
        NotificationChannel channel = new NotificationChannel(id, name, importance);
        channel.setDescription(description);
        context.getSystemService(NotificationManager.class).createNotificationChannel(channel);
    }
}