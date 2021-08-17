package pl.Guzooo.WoliumSpid;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import pl.Guzooo.WoliumSpid.Database.ProfileWithStages;
import pl.Guzooo.WoliumSpid.Database.Repository;

public class ProfileViewModel extends AndroidViewModel {
    private Repository repository;
    private int id = 0;
    private LiveData<ProfileWithStages> profile;

    public ProfileViewModel(Application application) {
        super(application);
        repository = new Repository(application);
    }

    public LiveData<ProfileWithStages> getProfile(int id){
        if(id == ProfileActivity.LAST_PROFILE)
            profile = repository.getLastProfile();
        else if(this.id != id)
            profile = repository.getProfile(id);
        this.id = id;
        return profile;
    }

    public int getProfileId(){
        return profile.getValue().getProfile().getId();
    }

    public int getCountOfStages(){
        return profile.getValue().getStages().size();
    }

    public void deleteProfile(){
        repository.deleteProfileWithStages(profile.getValue());
    }

    public void updateProfile(){
        repository.updateProfile(profile.getValue().getProfile());
    }

    public void updateStages(){
        repository.updateStages(profile.getValue().getStages());
    }
}
