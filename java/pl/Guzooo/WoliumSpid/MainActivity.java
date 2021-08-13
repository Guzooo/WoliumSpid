package pl.Guzooo.WoliumSpid;

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

public class MainActivity extends GActivity {

    private MainViewModel viewModel;
    private View addFAB;
    private View settingsFAB;

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
        setContentView(R.layout.activity_main);

        initialization();
        setFullScreen();
        setBusinessCard();
        setProfiles();
    }

    public void onClickAddProfile(View v){
        viewModel.addNewProfile();
        //TODO: otworz aktywnosc edycji profilu
    }

    public void onClickSettings(View v){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void initialization(){
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        addFAB = findViewById(R.id.add_fab);
        settingsFAB = findViewById(R.id.settings_fab);
    }

    private void setFullScreen(){
        View mainScroll = findViewById(R.id.main_scroll);
        FullScreenUtils.setUIVisibility(mainScroll);
        FullScreenUtils.setPaddings(mainScroll, this);
        FullScreenUtils.setApplyWindowInsets(addFAB, getWindowsInsetsListener());
    }

    private void setBusinessCard(){
        View logo = findViewById(R.id.logo_g);
        BusinessCard businessCard = findViewById(R.id.business_card_g);
        businessCard.setOpenerView(logo);
    }

    private void setProfiles(){
        AdapterProfile.ProfileListener profileListener = getProfileListener();
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

    private AdapterProfile.ProfileListener getProfileListener(){
        return new AdapterProfile.ProfileListener() {
            @Override
            public void onClickMainView(int id) {
                //TODO: otworz aktywność
            }

            @Override
            public void onClickPlay(ProfileWithStages profileWithStages) {
                //TODO: rozpocznij usługe
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