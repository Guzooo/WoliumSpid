package pl.Guzooo.WoliumSpid;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.IntDef;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LifecycleService;

import java.util.ArrayList;
import java.util.List;

import pl.Guzooo.Base.Utils.ColorUtils;
import pl.Guzooo.WoliumSpid.Database.Stage;
import pl.Guzooo.WoliumSpid.Utils.NotificationChannelUtils;
import pl.Guzooo.WoliumSpid.Utils.VolumeControllerUtils;

public class VolumeControllerService extends LifecycleService {

    @IntDef(value = {SMALLER_THAN, GREATER_OR_EQUAL_TO})
    private @interface CompareMode {
    }

    private static final int SMALLER_THAN = 0;
    private static final int GREATER_OR_EQUAL_TO = 1;

    public static final String EXTRA_ID = "extraid";

    private final int NOTIFICATION_ID = 30082020;
    private final int MINIMAL_MILLISECONDS_TO_LOCATION_CHANGE = 1000;
    private final int MINIMAL_DISTANCE_TO_LOCATION_CHANGE = 0;

    private final int EMERGENCY_STOP_NO_INFO_TEXT = 0;
    public static final int CURRENT_STAGE_UNSET = -1;

    private VolumeControllerData viewModel;
    private LocationManager locationManager;
    private LocationListener locationListener;
    Handler changeVolume = new Handler();

    private int currentStage = -1; //TODO: możliwe że przeniesienie zmiennych do view model mogłoby uławić prace po wznowieniu usługi // nie ma to znaczenia, ale może warto
    private ArrayList<Float> currentSpeeds = new ArrayList<>();
    private int timeWithoutProfile = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        viewModel = new VolumeControllerData(getApplication());
        VolumeControllerData.getIsWork().setValue(true);
        setLocationManager();
        startForeground(NOTIFICATION_ID, getNotification());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int profileId = 0;
        if(intent != null)
            profileId = intent.getIntExtra(EXTRA_ID, 0);
        if (profileId == 0)
            emergencyStop(EMERGENCY_STOP_NO_INFO_TEXT);
        else
            viewModel.getProfile(profileId).observe(this, profileWithStages -> {
                setCurrentStage(CURRENT_STAGE_UNSET);
                updateNotification();
            });
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        VolumeControllerData.resetAllCurrent();
        locationManager.removeUpdates(locationListener);
        super.onDestroy();
    }

    @SuppressLint("MissingPermission")
    private void setLocationManager() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = getLocationListener();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMAL_MILLISECONDS_TO_LOCATION_CHANGE, MINIMAL_DISTANCE_TO_LOCATION_CHANGE, locationListener);
    }

    private void emergencyStop (int optionalInfoText){
        if(optionalInfoText != EMERGENCY_STOP_NO_INFO_TEXT) {
            String text = getString(optionalInfoText);
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        }
        stopForeground(true);
        stopSelf();
    }

    private LocationListener getLocationListener(){
        return new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                addCurrentSpeed(location.getSpeed() *60*60/1000);//KilometersPerSeconds;
                controlVolume();
                updateNotification();
                supportForProfilelessSession();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                //Toast.makeText(getApplicationContext(), "Zmiana statusu na: " + status, Toast.LENGTH_SHORT).show();//TODO: poczytać
            }

            @Override
            public void onProviderEnabled(String provider) {
                //Toast.makeText(getApplicationContext(), "GPS Włączony", Toast.LENGTH_SHORT).show();//TODO: string, powiadomienie inne "WARNING"
            }

            @Override
            public void onProviderDisabled(String provider) {
                //Toast.makeText(getApplicationContext(), "GPS Wyłączony", Toast.LENGTH_SHORT).show();//TODO: string, powiadomienie inne "WARNING"
            }
        };
    }

    private void controlVolume(){
        List<Stage> stages = viewModel.getStages();
        int nextStage = currentStage + 1;
        int previewStage = currentStage - 1;
        if(stages.isEmpty())
            return;
        if(nextStage < stages.size() && compareCurrentSpeed(GREATER_OR_EQUAL_TO, stages.get(nextStage).getSpeed())) {
            setCurrentStage(nextStage);
            setVolumeByCurrentStage();
        } else if(previewStage >= 0 && compareCurrentSpeed(SMALLER_THAN, stages.get(currentStage).getSpeed())) {
            setCurrentStage(previewStage);
            setVolumeByCurrentStage();
        }
    }

    private void setCurrentStage(int newStage){
        currentStage = newStage;
        VolumeControllerData.getCurrentStage().setValue(newStage);
    }

    private void setVolumeByCurrentStage(){
        changeVolume.removeCallbacks(this::changeVolumeRunnable);
        changeVolume.post(this::changeVolumeRunnable);
    }

    private void changeVolumeRunnable(){
        if(currentStage < 0 || currentStage >= viewModel.getStages().size())
            return;
        Stage stage = viewModel.getStages().get(currentStage);
        AudioManager manager = (AudioManager) getSystemService(AUDIO_SERVICE);

        if(manager.getStreamVolume(AudioManager.STREAM_MUSIC) > stage.getVolume())
            manager.setStreamVolume(AudioManager.STREAM_MUSIC, manager.getStreamVolume(AudioManager.STREAM_MUSIC) - 1, 0);
        else if(manager.getStreamVolume(AudioManager.STREAM_MUSIC) < stage.getVolume())
            manager.setStreamVolume(AudioManager.STREAM_MUSIC, manager.getStreamVolume(AudioManager.STREAM_MUSIC) + 1, 0);

        if(manager.getStreamVolume(AudioManager.STREAM_MUSIC) != stage.getVolume())
            new Handler().postDelayed(this::changeVolumeRunnable, 300);
    }

    private void updateNotification(){
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, getNotification());
    }

    private Notification getNotification(){
        int accentColor = ColorUtils.getColorFromAttr(R.attr.colorGAccent, this);
        String title = viewModel.getProfileName(this);
        String text = getNotificationText();
        String finishString = getString(R.string.stop);
        PendingIntent finishIntent = VolumeControllerUtils.getStopPendingIntent(this);
        PendingIntent openIntent = VolumeControllerUtils.getOpenPendingIntent(this);
        return new NotificationCompat.Builder(this, NotificationChannelUtils.VOLUME_CONTROLLER_CHANNEL_ID)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setOnlyAlertOnce(true)
                .setShowWhen(false)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setSmallIcon(R.drawable.ic_notification_volume_controller)
                .setColor(accentColor)
                .setContentTitle(title)
                .setContentText(text)
                .addAction(R.drawable.ic_pause, finishString,  finishIntent)
                .setContentIntent(openIntent)
                .build();
    }

    private String getNotificationText(){
        if (currentStage == CURRENT_STAGE_UNSET)
            return getString(R.string.unset_stage_and_speed_info_with_separator, getCurrentSpeed());
        return getString(R.string.stage_and_speed_info_with_separator, currentStage+1, getCurrentSpeed());
    }

    private void addCurrentSpeed(float speed){
        if(currentSpeeds.size() == 5)
            currentSpeeds.remove(0);
        currentSpeeds.add(speed);
        VolumeControllerData.getCurrentSpeed().setValue(speed);
    }

    private boolean compareCurrentSpeed(@CompareMode int mode, float compareTo){
        int compatible = 0;
        for(int i = 0; i < currentSpeeds.size(); i++){
            if(mode == SMALLER_THAN && currentSpeeds.get(i) < compareTo)
                compatible++;
            else if(mode == GREATER_OR_EQUAL_TO && currentSpeeds.get(i) >= compareTo)
                compatible++;
        }
        if(compatible > currentSpeeds.size()/2)
            return true;
        return false;
    }

    private float getCurrentSpeed(){
        int lastIndex = currentSpeeds.size()-1;
        if(lastIndex < 0)
            return 0;
        return currentSpeeds.get(lastIndex);
    }

    private void supportForProfilelessSession(){
        List<Stage> stages = viewModel.getStages();
        if(stages.size() == 0)
            timeWithoutProfile++;
        else
            timeWithoutProfile = 0;
        if(timeWithoutProfile == 5)
            emergencyStop(R.string.profile_not_found);
    }
}