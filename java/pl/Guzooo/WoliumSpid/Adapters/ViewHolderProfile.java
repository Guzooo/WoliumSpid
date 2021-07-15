package pl.Guzooo.WoliumSpid.Adapters;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pl.Guzooo.WoliumSpid.Elements.VolumeBar;
import pl.Guzooo.WoliumSpid.R;

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

    public void setVolumeBar(int volumeBarNumber, int volume){
        volumeBars.get(volumeBarNumber);//TODO: ustaw wartość na volume;

    }

    public void setVolumeBarGone(int volumeBarNumber){
        volumeBars.get(volumeBarNumber).setVisibility(View.GONE);
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setOnClickMainViewListener(View.OnClickListener listener){
        itemView.setOnClickListener(listener);
    }

    public void setOnClickPlayListener(View.OnClickListener listener){
        play.setOnClickListener(listener);
    }

    private void initializationVolumeBars(View v){
        addVolumeBars(v);
        setVolumeBarsVisibility();
    }

    private void  addVolumeBars(View v){
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

    private void setVolumeBarsVisibility(){
        for(VolumeBar volumeBar : volumeBars)
            volumeBar.setVisibility(View.VISIBLE);
    }
}
