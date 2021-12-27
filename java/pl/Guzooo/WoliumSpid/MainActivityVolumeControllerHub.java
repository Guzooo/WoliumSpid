package pl.Guzooo.WoliumSpid;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;

import pl.Guzooo.WoliumSpid.Utils.VolumeControllerUtils;

public class MainActivityVolumeControllerHub {

    private View space;
    private TextView title;
    private TextView text;
    private ImageView pause;

    private int stage = -1;
    private float speed = 0;
    
    public void initialization(Activity activity){
        findElements(activity);
        setObservers(activity);
        setPause(activity);
        refreshText(activity);
    }
    
    private void findElements(Activity activity){
        space = activity.findViewById(R.id.right_space);
        title = activity.findViewById(R.id.right_space_title);
        text = activity.findViewById(R.id.right_space_text);
        pause = activity.findViewById(R.id.right_space_icon);
    }
    
    private void setObservers(Activity activity){
        VolumeControllerData.getIsWork().observe((LifecycleOwner) activity, this::setVisibility);
        VolumeControllerData.getCurrentTitle().observe((LifecycleOwner) activity, title::setText);
        VolumeControllerData.getCurrentStage().observe((LifecycleOwner) activity, integer -> {
            stage = integer;
            refreshText(activity);
        });
        VolumeControllerData.getCurrentSpeed().observe((LifecycleOwner) activity, aFloat -> {
            speed = aFloat;
            refreshText(activity);
        });
    }

    private void setPause(Context context){
        pause.setOnClickListener(view -> {
            VolumeControllerUtils.stop(context);
        });
    }

    private void setVisibility(boolean visible){
        if(visible) {
            space.setVisibility(View.VISIBLE);
            title.setVisibility(View.VISIBLE);
            text.setVisibility(View.VISIBLE);
            pause.setVisibility(View.VISIBLE);
        } else {
            space.setVisibility(View.GONE);
            title.setVisibility(View.GONE);
            text.setVisibility(View.GONE);
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
}
