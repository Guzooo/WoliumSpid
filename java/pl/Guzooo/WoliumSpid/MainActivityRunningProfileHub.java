package pl.Guzooo.WoliumSpid;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.LifecycleOwner;

import pl.Guzooo.WoliumSpid.Utils.NotificationChannelUtils;
import pl.Guzooo.WoliumSpid.Utils.VolumeControllerUtils;

public class MainActivityRunningProfileHub {

    private View space;
    private TextView title;
    private TextView text;
    private TextView info;
    private ImageView pause;
    private View clicableSpace;

    private int stage = -1;
    private float speed = 0;
    
    public void initialization(Activity activity){
        findElements(activity);
        setObservers(activity);
        setInfo();
        setPause(activity);
        refreshText(activity);
    }

    public void refreshInfoVisibility(Context context){
        if(isVisible()) {
            setInfoVisibility(context);
            setClickableSpace(context);
        }
    }

    private void findElements(Activity activity){
        space = activity.findViewById(R.id.right_space);
        title = activity.findViewById(R.id.running_profile_title);
        text = activity.findViewById(R.id.running_profile_text);
        info = activity.findViewById(R.id.running_profile_info);
        pause = activity.findViewById(R.id.running_profile_icon);
        clicableSpace = activity.findViewById(R.id.running_profile_clickable_space);
    }
    
    private void setObservers(Activity activity){
        VolumeControllerData.getIsWork().observe((LifecycleOwner) activity,
                                                profileRunning -> setVisibility(profileRunning, activity));
        VolumeControllerData.getCurrentTitle().observe((LifecycleOwner) activity,title::setText);
        VolumeControllerData.getCurrentStage().observe((LifecycleOwner) activity, integer -> {
            stage = integer;
            refreshText(activity);
        });
        VolumeControllerData.getCurrentSpeed().observe((LifecycleOwner) activity, aFloat -> {
            speed = aFloat;
            refreshText(activity);
        });
    }

    private void setInfo(){
        info.setText(R.string.volume_controller_channel_disabled);
    }

    private void setPause(Context context){
        pause.setOnClickListener(view -> {
            VolumeControllerUtils.stop(context);
        });
    }

    private void setVisibility(boolean visible, Context context){
        if(visible) {
            space.setVisibility(View.VISIBLE);
            title.setVisibility(View.VISIBLE);
            text.setVisibility(View.VISIBLE);
            pause.setVisibility(View.VISIBLE);
            setInfoVisibility(context);
            setClickableSpace(context);
        } else {
            space.setVisibility(View.GONE);
            title.setVisibility(View.GONE);
            text.setVisibility(View.GONE);
            info.setVisibility(View.GONE);
            pause.setVisibility(View.GONE);
        }
    }

    private void refreshText(Context context){
        String string;
        if(stage == VolumeControllerService.CURRENT_STAGE_UNSET)
            string = context.getString(R.string.unset_stage_and_speed_info_double_line, speed);
        else
            string = context.getString(R.string.stage_and_speed_info_double_line, stage+1, speed);
        text.setText(string);
    }

    private boolean isVisible(){
        if(title.getVisibility() == View.VISIBLE)
            return true;
        return false;
    }

    private void setInfoVisibility(Context context){
        if(NotificationChannelUtils.isChannelActive(NotificationChannelUtils.VOLUME_CONTROLLER_CHANNEL_ID, context))
            info.setVisibility(View.GONE);
        else
            info.setVisibility(View.VISIBLE);
    }

    private void setClickableSpace(Context context){
        if(info.getVisibility() == View.VISIBLE)
            clicableSpace.setOnClickListener(view -> NotificationChannelUtils.openChannelSettings(NotificationChannelUtils.VOLUME_CONTROLLER_CHANNEL_ID, context));
        else
            clicableSpace.setOnClickListener(null);
    }
}
