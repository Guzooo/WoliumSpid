package pl.Guzooo.WoliumSpid;

import android.os.Bundle;
import android.view.View;

import pl.Guzooo.Base.ModifiedElements.GActivity;
import pl.Guzooo.Base.Utils.FullScreenUtils;
import pl.Guzooo.Base.Utils.SettingsUtils;

public class SettingsActivity extends GActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setFullScreen();
        setFragment();
    }

    private void setFullScreen(){
        View mainScroll = findViewById(R.id.main_scroll);
        FullScreenUtils.setUIVisibility(mainScroll);
        FullScreenUtils.setPaddings(mainScroll, this);
    }

    private void setFragment(){
        View content = findViewById(R.id.content);
        SettingsUtils.setSettingsFragment(content, this);
    }
}