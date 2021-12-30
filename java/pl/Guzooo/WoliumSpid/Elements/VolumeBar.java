package pl.Guzooo.WoliumSpid.Elements;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import pl.Guzooo.WoliumSpid.R;

public class VolumeBar extends FrameLayout {

    private View upView;
    private View downView;

    public VolumeBar(Context context, AttributeSet attrs){
        super(context, attrs);
        initialization();
    }

    private void initialization(){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.volume_bar, this, true);

        ViewGroup linearLayout = findViewById(R.id.volume_bar);
        upView = linearLayout.getChildAt(0);
        downView = linearLayout.getChildAt(1);
    }

    public void setLevel(int level, int maxLevel){
        if(level == 0){
            downView.setVisibility(GONE);
            return;
        }
        downView.setVisibility(VISIBLE);

        float weightDown = (float) level/maxLevel;
        float weightUp = 1 - weightDown;
        LinearLayout.LayoutParams paramsUp = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, weightUp);
        LinearLayout.LayoutParams paramsDown = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, weightDown);
        upView.setLayoutParams(paramsUp);
        downView.setLayoutParams(paramsDown);
    }
}
