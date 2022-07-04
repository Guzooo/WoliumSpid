package pl.Guzooo.WoliumSpid.Adapters;


import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import pl.Guzooo.WoliumSpid.Database.Stage;
import pl.Guzooo.WoliumSpid.R;
import pl.Guzooo.WoliumSpid.Utils.VolumeUtils;

public class ViewHolderStage extends RecyclerView.ViewHolder {

    private TextView title;
    private ImageView volumeIcon;
    private TextView volumeText;
    private TextView speedNext;
    private TextView speedBack;
    private View leftArrow;
    private View rightArrow;
    private View leftSkipArrow;
    private View rightSkipArrow;
    private View active;

    public ViewHolderStage(View v){
        super(v);
        title = v.findViewById(R.id.title);
        volumeIcon = v.findViewById(R.id.volume_image);
        volumeText = v.findViewById(R.id.volume_title);
        speedNext = v.findViewById(R.id.speed_start);
        speedBack = v.findViewById(R.id.speed_end);
        leftArrow = v.findViewById(R.id.left_arrow);
        rightArrow = v.findViewById(R.id.right_arrow);
        leftSkipArrow = v.findViewById(R.id.left_shadow_arrow);
        rightSkipArrow = v.findViewById(R.id.right_shadow_arrow);
        active = v.findViewById(R.id.active);
    }

    public void setStageInfo(Stage stage){
        int order = stage.getOrder();
        int volume = stage.getVolume();
        float speedNext = stage.getSpeedNext();
        float speedBack = stage.getRealSpeedBack();
        boolean active = stage.isActive();
        boolean skipLeft = stage.isSkipNext();
        boolean skipRight = stage.isSkipBack();
        boolean last = stage.isLast();
        setTitle(order);
        setVolumeText(volume);
        setVolumeIcon(volume);
        setSpeedNext(speedNext);
        setSpeedBack(speedBack);
        setActive(active);
        setLeftSkipArrow(skipLeft);
        setRightSkipArrow(skipRight);
        setRightArrow(last);
    }

    public void setOnClickMainViewListener(View.OnClickListener listener){
        itemView.setOnClickListener(listener);
    }

    private void setTitle(int order){
        String text = getContext().getString(R.string.stage_order, order);
        title.setText(text);
    }

    private void setVolumeText(int volume){
        String text = Integer.toString(volume);
        volumeText.setText(text);
    }

    private void setVolumeIcon(int volume){
        if(volume == 0)
            volumeIcon.setImageResource(R.drawable.ic_volume_mute);
        else if(volume < (float) VolumeUtils.getVolumeMax(getContext()) / 2)
            volumeIcon.setImageResource(R.drawable.ic_volume_low);
        else
            volumeIcon.setImageResource(R.drawable.ic_volume_high);
    }

    private void setSpeedNext(float speed){
        String text = getContext().getString(R.string.speed_next_turn_on, speed);
        speedNext.setText(text);
    }

    private void setSpeedBack(float speed){
        String text = getContext().getString(R.string.speed_back_turn_on, speed);
        speedBack.setText(text);
    }

    private void setActive(boolean isActive){
        if(isActive)
            active.setVisibility(View.VISIBLE);
        else
            active.setVisibility(View.INVISIBLE);
    }

    private void setRightArrow(boolean isLastStage){
        if(isLastStage) {
            rightArrow.setVisibility(View.GONE);
            speedBack.setVisibility(View.INVISIBLE);
        } else {
            rightArrow.setVisibility(View.VISIBLE);
            speedBack.setVisibility(View.VISIBLE);
        }
    }

    private void setLeftSkipArrow(boolean visible){
        if(visible)
            leftSkipArrow.setVisibility(View.VISIBLE);
        else
            leftSkipArrow.setVisibility(View.GONE);
    }

    private void setRightSkipArrow(boolean visible){
        if(visible)
            rightSkipArrow.setVisibility(View.VISIBLE);
        else
            rightSkipArrow.setVisibility(View.GONE);
    }

    private Context getContext(){
        return itemView.getContext();
    }
}
