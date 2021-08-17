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
    private TextView speedStart;
    private View topArrows;
    private View centerArrows;
    private View active;

    public ViewHolderStage(View v){
        super(v);
        title = v.findViewById(R.id.title);
        volumeIcon = v.findViewById(R.id.volume_image);
        volumeText = v.findViewById(R.id.volume_title);
        speedStart = v.findViewById(R.id.speed_start);
        topArrows = v.findViewById(R.id.top_arrows);
        centerArrows = v.findViewById(R.id.center_arrows);
        active = v.findViewById(R.id.active);
        setArrows();
        setActive();
    }

    public void setStageInfo(Stage stage){
        int order = stage.getOrder();
        int volume = stage.getVolume();
        float speed = stage.getSpeed();
        setTitle(order);
        setVolumeText(volume);
        setVolumeIcon(volume);
        setSpeed(speed);
    }

    public void setGoneTopArrows(){
        topArrows.setVisibility(View.GONE);
    }

    public void setVisibleCenterArrows(){
        centerArrows.setVisibility(View.VISIBLE);
    }

    public void setVisibleActive(){
        active.setVisibility(View.VISIBLE);
    }

    public void setOnClickMainViewListener(View.OnClickListener listener){
        itemView.setOnClickListener(listener);
    }

    private void setArrows(){
        topArrows.setVisibility(View.VISIBLE);
        centerArrows.setVisibility(View.INVISIBLE);
    }

    private void setActive(){
        active.setVisibility(View.INVISIBLE);
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

    private void setSpeed(float speed){
        String text = getContext().getString(R.string.speed_start, speed);
        speedStart.setText(text);
    }

    private Context getContext(){
        return itemView.getContext();
    }
}
