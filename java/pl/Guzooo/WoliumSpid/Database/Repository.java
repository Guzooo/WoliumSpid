package pl.Guzooo.WoliumSpid.Database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class Repository {
    private ProfileDao profileDao;
    private StageDao stageDao;

    private LiveData<List<ProfileWithStages>> allProfiles;

    public Repository (Application application){
        WSDatabase database = WSDatabase.getInstance(application);
        profileDao = database.profileDao();
        stageDao = database.stageDao();
        allProfiles = profileDao.getAllProfilesWithStages();
    }

    public void insertProfile(Profile profile){
        WSDatabase.getExecutor().execute(() -> {
            profileDao.insert(profile);
        });
    }

    public void updateProfile(Profile profile){
        WSDatabase.getExecutor().execute(() -> {
            profileDao.update(profile);
        });
    }

    public void deleteProfileWithStages(ProfileWithStages profile){
        WSDatabase.getExecutor().execute(() -> {
            profileDao.delete(profile.getProfile(), profile.getStages());
        });
    }

    public void insertStage(Stage stage){
        WSDatabase.getExecutor().execute(() -> {
            stageDao.insert(stage);
        });
    }

    public void updateStage(Stage stage){
        WSDatabase.getExecutor().execute(() -> {
            stageDao.update(stage);
        });
    }

    public void updateStages(List<Stage> stages){
        WSDatabase.getExecutor().execute(() -> {
            stageDao.updateAll(stages);
        });
    }

    public void deleteStage(Stage stage){
        WSDatabase.getExecutor().execute(() -> {
            stageDao.delete(stage);
        });
    }

    public LiveData<List<ProfileWithStages>> getAllProfiles(){
        return allProfiles;
    }

    public LiveData<ProfileWithStages> getProfile(int id){
        return profileDao.getOneProfileWithStages(id);
    }

    public LiveData<ProfileWithStages> getLastProfile(){
        return profileDao.getLast();
    }

    public Stage getStageFromOneProfileByAdapterPosition(int profileId, int positionStart){
        return stageDao.getStageFromOneProfileByAdapterPosition(profileId, positionStart);
    }

    public List<Stage> getStagesFromOneProfileWithOrderGreaterOrEqualsThan(int profileId, int orderMin){
        return stageDao.getStagesFromOneProfileWithOrderGreaterOrEqualsThan(profileId, orderMin);
    }

    public List<Stage> getStagesFromOneProfileWithOrderBetween(int profileId, int orderMin, int orderMax){
        return stageDao.getStagesFromOneProfileWithOrderBetween(profileId, orderMin, orderMax);
    }

    public float getSpeedNext(int profileId, int order){
        return stageDao.getSpeedNext(profileId, order);
    }
}