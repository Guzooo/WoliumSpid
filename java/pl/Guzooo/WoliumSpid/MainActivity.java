package pl.Guzooo.WoliumSpid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pl.Guzooo.Base.Elements.BusinessCard;
import pl.Guzooo.Base.ModifiedElements.GActivity;
import pl.Guzooo.Base.Utils.FullScreenUtils;
import pl.Guzooo.Base.Utils.ThemeUtils;
import pl.Guzooo.WoliumSpid.Adapters.AdapterProfile;
import pl.Guzooo.WoliumSpid.Database.ProfileWithStages;
import pl.Guzooo.WoliumSpid.Utils.NotificationChannelUtils;
import pl.Guzooo.WoliumSpid.Utils.PermissionsRequestUtils;
import pl.Guzooo.WoliumSpid.Utils.VolumeControllerUtils;

public class MainActivity extends GActivity {

    private MainViewModel viewModel;
    private View addFAB;
    private View settingsFAB;

    private MainActivityVolumeControllerHub rightSpace = new MainActivityVolumeControllerHub();

    @Override
    public int getBottomPadding() {
        int bottom = addFAB.getHeight();
        bottom += getMarginBiggest() *2;
        return bottom;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtils.setTheme(this);
        NotificationChannelUtils.createNotificationChannels(this);
        setContentView(R.layout.activity_main);

        initialization();
        setFullScreen();
        setBusinessCard();
        setProfiles();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsRequestUtils.localizationOnRunProfile(requestCode, grantResults, this);
    }

    public void onClickAddProfile(View v){
        viewModel.addNewProfile();
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(ProfileActivity.EXTRA_ID, ProfileActivity.LAST_PROFILE);
        startActivity(intent);
    }

    public void onClickSettings(View v){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void initialization(){
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        addFAB = findViewById(R.id.add_fab);
        settingsFAB = findViewById(R.id.settings_fab);
        rightSpace.initialization(this);
    }

    private void setFullScreen(){
        View mainScroll = findViewById(R.id.main_scroll);
        FullScreenUtils.setUIVisibility(addFAB);
        FullScreenUtils.setApplyWindowInsets(addFAB, getWindowsInsetsListener());
        FullScreenUtils.setPaddings(mainScroll, this);
    }

    private void setBusinessCard(){
        View logo = findViewById(R.id.logo_g);
        BusinessCard businessCard = findViewById(R.id.business_card_g);
        businessCard.setOpenerView(logo);
    }

    private void setProfiles(){
        AdapterProfile.ProfileListener profileListener = getProfileListener(this);
        AdapterProfile adapterProfile = new AdapterProfile(profileListener);
        viewModel.getProfilesWithStages().observe(this, getObserverProfile(adapterProfile));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerProfile = findViewById(R.id.recycler_view);
        recyclerProfile.setLayoutManager(layoutManager);
        recyclerProfile.setAdapter(adapterProfile);
    }

    private OnApplyWindowInsetsListener getWindowsInsetsListener(){
        return new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                setInsets(insets);
                setAddFabSpacing(insets);
                setSettingsFabSpacing(insets);
                return insets;
            }
        };
    }

    private void setAddFabSpacing(WindowInsetsCompat insets){
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) addFAB.getLayoutParams();
        layoutParams.bottomMargin = insets.getSystemWindowInsetBottom() + getMarginBiggest();
    }

    private void setSettingsFabSpacing(WindowInsetsCompat insets){
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) settingsFAB.getLayoutParams();
        layoutParams.bottomMargin = insets.getSystemWindowInsetBottom() + getMarginBiggest();
        layoutParams.leftMargin = insets.getSystemWindowInsetLeft() + getMarginBiggest();
    }

    private int getMarginBiggest(){
        return getResources().getDimensionPixelOffset(R.dimen.margin_biggest);
    }

    private AdapterProfile.ProfileListener getProfileListener(Activity activity){
        return new AdapterProfile.ProfileListener() {
            @Override
            public void onClickMainView(int id) {
                Intent intent = new Intent(activity, ProfileActivity.class);
                intent.putExtra(ProfileActivity.EXTRA_ID, id);
                startActivity(intent);
            }

            @Override
            public void onClickPlay(int id) {
                VolumeControllerUtils.run(id, activity);
            }
        };
    }

    private Observer<List<ProfileWithStages>> getObserverProfile(AdapterProfile adapter){
        return new Observer<List<ProfileWithStages>>() {
            @Override
            public void onChanged(List<ProfileWithStages> profilesWithStages) {
                adapter.submitList(profilesWithStages);
            }
        };
    }
}