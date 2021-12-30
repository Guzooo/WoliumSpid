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
import pl.Guzooo.WoliumSpid.R;

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

    @SuppressLint("NewApi")
    public static boolean isChannelActive(@Channels String channelId, Context context){
        if(FragmentationUtils.isMinimumOreo())
            return isChannelActiveOnOreoAndNewerAndroids(channelId, context);
        return isChannelActiveOnAndroidsBeforeOreo(context);
    }

    @SuppressLint("NewApi")
    public static void openChannelSettings(@Channels String channelId, Context context){
        if(FragmentationUtils.isMinimumOreo())
            openChannelSettingsOnOreoAndNewerAndroids(channelId, context);
        else
            openChannelSettingsOnAndroidsBeforeOreo(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void createVolumeControllerChannel(Context context){
        int name = R.string.volume_controller_channel_name;
        int description = R.string.volume_controller_channel_description;
        int importance = NotificationManagerCompat.IMPORTANCE_LOW;
        createChannel(VOLUME_CONTROLLER_CHANNEL_ID, name, description, importance, context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void createChannel(String id, int nameId, int descriptionId, int importance, Context context){
        String name = context.getString(nameId);
        String description = context.getString(descriptionId);
        NotificationChannel channel = new NotificationChannel(id, name, importance);
        channel.setDescription(description);
        context.getSystemService(NotificationManager.class).createNotificationChannel(channel);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static boolean isChannelActiveOnOreoAndNewerAndroids(String channelId, Context context){
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = manager.getNotificationChannel(channelId);
        if (channel.getImportance() == NotificationManagerCompat.IMPORTANCE_NONE)
            return false;
        return true;
    }

    private static boolean isChannelActiveOnAndroidsBeforeOreo(Context context){
        return NotificationManagerCompat.from(context).areNotificationsEnabled();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void openChannelSettingsOnOreoAndNewerAndroids(@Channels String channelId, Context context){
        Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
        intent.putExtra(Settings.EXTRA_CHANNEL_ID, channelId);
        context.startActivity(intent);
    }

    private static void openChannelSettingsOnAndroidsBeforeOreo(Context context){//TODO:test czy działa + czy działa na najstarszym androidzie
        Intent intent = new Intent("android.settings.APP_NOTIFICATION_SETTINGS");// zrobić tak, że w androidzie >= 8 jak wyłaczony kanał to do info o kanale,  a jak ogol to do ogolu otwiera
        intent.putExtra("app_package", context.getPackageName());
        intent.putExtra("app_uid", context.getApplicationInfo().uid);
        context.startActivity(intent);
    }
}