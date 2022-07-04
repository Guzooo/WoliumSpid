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
import pl.Guzooo.WoliumSpid.Database.WSDatabase;
import pl.Guzooo.WoliumSpid.Utils.PermissionsRequestUtils;
import pl.Guzooo.WoliumSpid.Utils.VolumeControllerUtils;

public class ProfileActivity extends GActivity {

    public final static String EXTRA_ID = "extraid";
    public final static int LAST_PROFILE = 0;

    private int id;
    private ProfileViewModel profileViewModel;
    private StageViewModel stageViewModel;
    private TitleChanger titleChanger;
    private View addFab;
    private RecyclerView recyclerStage;
    private AdapterStage adapterStage;
    private int lastStage = -1;

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
        setActiveDependence();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean work = isThisProfileWork();
        boolean haveStages = adapterStage.getCurrentList().size() > 0;
        menu.findItem(R.id.stop).setVisible(work);
        menu.findItem(R.id.run).setVisible(!work && haveStages);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit_title:
                titleChanger.show();
                return true;
            case R.id.run:
                VolumeControllerUtils.run(id, this);
                return true;
            case R.id.stop:
                VolumeControllerUtils.stop(this);
                return true;
            case R.id.delete:
                profileViewModel.deleteProfile();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsRequestUtils.localizationOnRunProfile(requestCode, grantResults, this);
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
        adapterStage = new AdapterStage(stageListener);
        id = getIntent().getIntExtra(EXTRA_ID, -1);
        profileViewModel.getProfile(id).observe(this, getObserverStage(adapterStage));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerStage.setLayoutManager(layoutManager);
        recyclerStage.setAdapter(adapterStage);
    }

    private void setActiveDependence(){
        VolumeControllerData.getIsWork().observe(this, aBoolean -> {
            markActiveStages();
            invalidateOptionsMenu();
        });
        VolumeControllerData.getCurrentId().observe(this, integer -> markActiveStages());
        VolumeControllerData.getCurrentStage().observe(this, integer -> markActiveStages());
    }

    private int getMarginBiggest(){
        return getResources().getDimensionPixelOffset(R.dimen.margin_biggest);
    }

    private OnApplyWindowInsetsListener getWindowsInsertsListener(){
        return (v, insets) -> {
            setInsets(insets);
            setAddFabSpacing(insets);
            return insets;
        };
    }

    private void setAddFabSpacing(WindowInsetsCompat insets){
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) addFab.getLayoutParams();
        layoutParams.bottomMargin = insets.getSystemWindowInsetBottom() + getMarginBiggest();
    }

    private AdapterStage.StageListener getStageListener(){
        return stage -> {
            Stage newStage = stage.duplicate();
            stageViewModel.setMaxOrder(profileViewModel.getCountOfStages());
            stageViewModel.setStage(newStage);
            new StageFragment().show(getSupportFragmentManager());
        };
    }

    private Observer<ProfileWithStages> getObserverStage(AdapterStage adapter){
        return profileWithStages -> {
            if (profileWithStages == null) {
                finish();
                return;
            }
                Profile profile = profileWithStages.getProfile();
                List<Stage> stages = profileWithStages.getStages();
                getSupportActionBar().setTitle(profile.getName(getApplicationContext()));
                titleChanger.setEditText(profile.getName());
                invalidateOptionsMenu();
                //TODO: animacja
            WSDatabase.getExecutor().execute(() -> {
                setStagesDependence(stages);
                adapter.submitList(stages, this::markActiveStages);
            });

        };
    }

    private void setStagesDependence(List<Stage> stages){
        for(int i = 0; i < stages.size(); i++) {
            Stage stage = stages.get(i);
            if(i+1 == stages.size())
                stage.setLast(true);
            else
                stage.setRealSpeedBack(stages.get(i+1));
        }
        setStagesDependenceOfSkipping(stages);
    }

    private void setStagesDependenceOfSkipping(List<Stage> stages){
        if(stages.size() < 2)
            return;
        float lastSpeed = stages.get(0).getSpeedNext();
        for(int i = 1; i < stages.size(); i++){
            Stage previousStage = stages.get(i-1);
            Stage stage = stages.get(i);
            if(stage.getSpeedNext() <= lastSpeed)
                previousStage.setSkipNext(true);
            else
                lastSpeed = stage.getSpeedNext();
        }
        lastSpeed = stages.get(stages.size()-2).getRealSpeedBack();
        for(int i = stages.size()-3; i >= 0; i--){
            Stage previousStage = stages.get(i+1);
            Stage stage = stages.get(i);
            if(stage.getRealSpeedBack() >= lastSpeed)
                previousStage.setSkipBack(true);
            else
                lastSpeed = stage.getRealSpeedBack();
        }
    }

    private void markActiveStages(){
        if(isThisProfileWork()){
            int activeStage = VolumeControllerData.getCurrentStage().getValue();
            setActive(lastStage, false);
            setActive(activeStage, true);
            lastStage = activeStage;
        } else if (!VolumeControllerData.getIsWork().getValue())
            setActive(lastStage, false);
    }

    private boolean isThisProfileWork(){
        if(VolumeControllerData.getCurrentId().getValue() != id)
            return false;
        if(!VolumeControllerData.getIsWork().getValue())
            return false;
        return true;
    }

    private void setActive(int stage, boolean active){
        if (stage >= 0 && stage < adapterStage.getCurrentList().size()) {
            adapterStage.getCurrentList().get(stage).setActive(active);
            adapterStage.notifyItemChanged(stage);
        }
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