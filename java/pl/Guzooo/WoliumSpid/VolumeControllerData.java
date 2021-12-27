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

    private Repository repository;
    private int id = 0;
    private LiveData<ProfileWithStages> profile = new MutableLiveData<>();
    private static MutableLiveData<Boolean> isWork = new MutableLiveData<>(false);
    private static MutableLiveData<String> currentTitle = new MutableLiveData<>("");
    private static MutableLiveData<Integer> currentStage = new MutableLiveData<>(VolumeControllerService.CURRENT_STAGE_UNSET);
    private static MutableLiveData<Float> currentSpeed = new MutableLiveData<>(0f);

    public VolumeControllerData(Application application) {
        repository = new Repository(application);
    }

    public LiveData<ProfileWithStages> getProfile(int id){
        if(this.id != id) {
            profile = repository.getProfile(id);
            this.id = id;
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
}
