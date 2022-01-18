package pl.Guzooo.WoliumSpid;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

import pl.Guzooo.WoliumSpid.Database.Profile;
import pl.Guzooo.WoliumSpid.Database.ProfileWithStages;
import pl.Guzooo.WoliumSpid.Database.Repository;

public class MainViewModel extends AndroidViewModel {
    private Repository repository;
    private LiveData<List<ProfileWithStages>> profiles;

    public MainViewModel(Application application) {
        super(application);
        repository = new Repository(application);
        profiles = repository.getAllProfiles();
    }

    public LiveData<List<ProfileWithStages>> getProfilesWithStages(){
        return profiles;
    }

    public void addNewProfile(){
        Profile profile = new Profile();
        repository.insertProfile(profile);
    }
}
