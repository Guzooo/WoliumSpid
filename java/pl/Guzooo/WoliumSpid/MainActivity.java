package pl.Guzooo.WoliumSpid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.WindowInsetsCompat;

import pl.Guzooo.Base.Elements.BusinessCard;
import pl.Guzooo.Base.ModifiedElements.GActivity;
import pl.Guzooo.Base.Utils.FullScreenUtils;
import pl.Guzooo.Base.Utils.ThemeUtils;

public class MainActivity extends GActivity {

    View addFAB;
    View settingsFAB;

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
        setProfilesRecycler();
    }

    public void onClickAddProfile(View v){

    }

    public void onClickSettings(View v){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void initialization(){
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

    private void setProfilesRecycler(){

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
}