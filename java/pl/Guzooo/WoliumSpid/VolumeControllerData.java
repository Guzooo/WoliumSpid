package pl.Guzooo.WoliumSpid;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import pl.Guzooo.WoliumSpid.Database.ProfileWithStages;
import pl.Guzooo.WoliumSpid.Database.Repository;
import pl.Guzooo.WoliumSpid.Database.Stage;

public class VolumeControllerData {

    //TODO:stała z service przenissc tutuaj

    private static final int UNWORKED_PROFILE = 0;

    private static MutableLiveData<Integer> currentId = new MutableLiveData<>(UNWORKED_PROFILE);
    private static MutableLiveData<Boolean> isWork = new MutableLiveData<>(false);
    private static MutableLiveData<String> currentTitle = new MutableLiveData<>("");
    private static MutableLiveData<Integer> currentStage = new MutableLiveData<>(VolumeControllerService.CURRENT_STAGE_UNSET);
    private static MutableLiveData<Float> currentSpeed = new MutableLiveData<>(0f);
    private Repository repository;
    private LiveData<ProfileWithStages> profile = new MutableLiveData<>();

    public VolumeControllerData(Application application) {
        repository = new Repository(application);
    }

    public LiveData<ProfileWithStages> getProfile(int id){
        if(currentId.getValue() != id) {
            profile = repository.getProfile(id);
            getCurrentId().setValue(id);
        }
        return profile;
    }

    public String getProfileName(Context context){
        String name;
        if(profile == null || profile.getValue() == null)
            name = context.getString(R.string.looking_profile);
        else
            name = profile.getValue().getProfile().getName(context);
        if(!currentTitle.getValue().equals(name))
            currentTitle.setValue(name);
        return name;
    }

    public List<Stage> getStages(){
        if(profile == null || profile.getValue() == null)
            return new ArrayList<>();
        return profile.getValue().getStages();
    }

    public static MutableLiveData<Integer> getCurrentId(){
        return currentId;
    }

    public static MutableLiveData<Boolean> getIsWork(){
        return isWork;
    }

    public static MutableLiveData<String> getCurrentTitle(){
        return currentTitle;
    }

    public static MutableLiveData<Integer> getCurrentStage(){
        return currentStage;
    }

    public static MutableLiveData<Float> getCurrentSpeed(){
        return currentSpeed;
    }

    public static boolean isWork(){
        if(currentId.getValue() == UNWORKED_PROFILE)
            return false;
        return true;
    }

    public static void resetAllCurrent(){
        currentId.setValue(UNWORKED_PROFILE);
        isWork.setValue(false);
        currentTitle.setValue("");
        currentStage.setValue(VolumeControllerService.CURRENT_STAGE_UNSET);
        currentSpeed.setValue(0f);
    }
}
