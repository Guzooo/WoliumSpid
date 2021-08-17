package pl.Guzooo.WoliumSpid;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pl.Guzooo.Base.Elements.TitleChanger;
import pl.Guzooo.Base.ModifiedElements.GActivity;
import pl.Guzooo.Base.Utils.FullScreenUtils;
import pl.Guzooo.WoliumSpid.Adapters.AdapterStage;
import pl.Guzooo.WoliumSpid.Database.Profile;
import pl.Guzooo.WoliumSpid.Database.ProfileWithStages;
import pl.Guzooo.WoliumSpid.Database.Stage;

public class ProfileActivity extends GActivity {

    public final static String EXTRA_ID = "extraid";
    public final static int LAST_PROFILE = 0;

    private ProfileViewModel profileViewModel;
    private StageViewModel stageViewModel;
    private TitleChanger titleChanger;
    private View addFab;
    private RecyclerView recyclerStage;

    @Override
    public int getBottomPadding() {
        int bottom = addFab.getHeight();
        bottom += getMarginBiggest() *2;
        return bottom;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initialization();
        setFullScreen();
        setTitleChanger();
        setStages();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit_title:
                titleChanger.show();
                return true;
            case R.id.run:
                //TODO: włącz profil
                return true;
            case R.id.delete:
                profileViewModel.deleteProfile();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if(titleChanger.isVisible())
            titleChanger.hide();
        else
            super.onBackPressed();
    }

    public void onClickAddStage(View v){
        Stage stage = new Stage();
        stage.setProfileId(profileViewModel.getProfileId());
        stageViewModel.setMaxOrder(profileViewModel.getCountOfStages()+1);
        stageViewModel.setStage(stage);
        new StageFragment().show(getSupportFragmentManager());
    }

    private void initialization(){
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        stageViewModel = new ViewModelProvider(this).get(StageViewModel.class);
        titleChanger = findViewById(R.id.title_changer);
        addFab = findViewById(R.id.add_fab);
        recyclerStage = findViewById(R.id.recycler_view);
    }

    private void setFullScreen(){
        FullScreenUtils.setUIVisibility(addFab);
        FullScreenUtils.setApplyWindowInsets(addFab, getWindowsInsertsListener());
        FullScreenUtils.setPaddings(recyclerStage, this);
    }

    private void setTitleChanger(){
        titleChanger.setHint(R.string.profile_title_hint);
        titleChanger.setViewInFrontOfActionBar();
        titleChanger.setListener(getTitleChangeListener());
    }

    private void setStages(){
        AdapterStage.StageListener stageListener = getStageListener();
        AdapterStage adapterStage = new AdapterStage(stageListener);
        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        profileViewModel.getProfile(id).observe(this, getObserverStage(adapterStage));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerStage.setLayoutManager(layoutManager);
        recyclerStage.setAdapter(adapterStage);
    }

    private int getMarginBiggest(){
        return getResources().getDimensionPixelOffset(R.dimen.margin_biggest);
    }

    private OnApplyWindowInsetsListener getWindowsInsertsListener(){
        return new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                setInsets(insets);
                setAddFabSpacing(insets);
                return insets;
            }
        };
    }

    private void setAddFabSpacing(WindowInsetsCompat insets){
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) addFab.getLayoutParams();
        layoutParams.bottomMargin = insets.getSystemWindowInsetBottom() + getMarginBiggest();
    }

    private AdapterStage.StageListener getStageListener(){
        return new AdapterStage.StageListener() {
            @Override
            public void onClick(Stage stage) {
                Stage newStage = stage.clone();
                stageViewModel.setMaxOrder(profileViewModel.getCountOfStages());
                stageViewModel.setStage(newStage);
                new StageFragment().show(getSupportFragmentManager());
            }
        };
    }

    private Observer<ProfileWithStages> getObserverStage(AdapterStage adapter){
        return new Observer<ProfileWithStages>() {
            @Override
            public void onChanged(ProfileWithStages profileWithStages) {
                if (profileWithStages == null) {
                    finish();
                    return;
                }
                Profile profile = profileWithStages.getProfile();
                List<Stage> stages = profileWithStages.getStages();
                adapter.submitList(stages);
                getSupportActionBar().setTitle(profile.getName(getApplicationContext()));
                titleChanger.setEditText(profile.getName());
                //TODO: animacja
            }
        };
    }

    private TitleChanger.TitleChangerListener getTitleChangeListener(){
        return new TitleChanger.TitleChangerListener() {
            @Override
            public void onClickPositiveButton(String newTitle) {
                profileViewModel.getProfile().setName(newTitle);
                profileViewModel.updateProfile();
            }

            @Override
            public void onClickNegativeButton() {
                String text = profileViewModel.getProfile().getName();
                titleChanger.setEditText(text);
            }
        };
    }
}