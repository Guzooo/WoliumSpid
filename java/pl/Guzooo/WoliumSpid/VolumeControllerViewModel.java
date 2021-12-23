package pl.Guzooo.WoliumSpid;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

import pl.Guzooo.WoliumSpid.Database.ProfileWithStages;
import pl.Guzooo.WoliumSpid.Database.Repository;
import pl.Guzooo.WoliumSpid.Database.Stage;

public class VolumeControllerViewModel extends AndroidViewModel {

    private Repository repository;
    private int id = 0;
    private LiveData<ProfileWithStages> profile;

    public VolumeControllerViewModel(Application application) {
        super(application);
        repository = new Repository(application);
    }

    public LiveData<ProfileWithStages> getProfile(int id){
        if(this.id != id)
            profile = repository.getProfile(id);
        this.id = id;
        return profile;
    }

    public String getProfileName(Context context){
        if(profile == null || profile.getValue() == null)
            return context.getString(R.string.looking_profile);
        return profile.getValue().getProfile().getName(context);
    }

    public List<Stage> getStages(){
        if(profile == null || profile.getValue() == null)
            return new ArrayList<>();
        return profile.getValue().getStages();
    }
}
