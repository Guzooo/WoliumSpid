package pl.Guzooo.WoliumSpid.Adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pl.Guzooo.WoliumSpid.Database.Profile;
import pl.Guzooo.WoliumSpid.Database.Stage;
import pl.Guzooo.WoliumSpid.Elements.VolumeBar;
import pl.Guzooo.WoliumSpid.R;
import pl.Guzooo.WoliumSpid.Utils.VolumeUtils;

public class ViewHolderProfile extends RecyclerView.ViewHolder {

    private ArrayList<VolumeBar> volumeBars = new ArrayList<>();
    private TextView title;
    private View play;

    public ViewHolderProfile(View v){
        super(v);
        initializationVolumeBars(v);
        title = v.findViewById(R.id.title);
        play = v.findViewById(R.id.play);
    }
    
    public void setVolumeBars(List<Stage> stages){
        Context context = itemView.getContext();
        int maxVolume = VolumeUtils.getVolumeMax(context);
        for(int i = 0; i < volumeBars.size(); i++){
            if(i < stages.size()) {
                Stage stage = stages.get(i);
                int volume = stage.getVolume();
                setVolumeBar(i, volume, maxVolume);
            } else
                setVolumeBar(i, -1, 0);
        }
    }
    
    public void setTitle(Profile profile){
        Context context = itemView.getContext();
        String name = profile.getName(context);
        title.setText(name);
    }

    public void setPlay(boolean canPlay){
        if(canPlay)
            play.setAlpha(1f);
        else
            play.setAlpha(0.5f);
    }

    public void setOnClickMainViewListener(View.OnClickListener listener){
        itemView.setOnClickListener(listener);
    }

    public void setOnClickPlayListener(View.OnClickListener listener){
        play.setOnClickListener(listener);
    }

    private void initializationVolumeBars(View v){
        volumeBars.add(v.findViewById(R.id.volume_bar_1));
        volumeBars.add(v.findViewById(R.id.volume_bar_2));
        volumeBars.add(v.findViewById(R.id.volume_bar_3));
        volumeBars.add(v.findViewById(R.id.volume_bar_4));
        volumeBars.add(v.findViewById(R.id.volume_bar_5));
        volumeBars.add(v.findViewById(R.id.volume_bar_6));
        volumeBars.add(v.findViewById(R.id.volume_bar_7));
        volumeBars.add(v.findViewById(R.id.volume_bar_8));
        volumeBars.add(v.findViewById(R.id.volume_bar_9));
        volumeBars.add(v.findViewById(R.id.volume_bar_10));
    }

    private void setVolumeBar(int i, int volume, int maxVolume){
        VolumeBar volumeBar = volumeBars.get(i);
        if(volume != -1) {
            volumeBar.setVisibility(View.VISIBLE);
            volumeBar.setLevel(volume, maxVolume);
        } else
            volumeBar.setVisibility(View.INVISIBLE);
    }
}
