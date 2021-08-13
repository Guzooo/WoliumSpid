package pl.Guzooo.WoliumSpid.Database;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.Collections;
import java.util.List;

public class ProfileWithStages {
    @Embedded private Profile profile;
    @Relation(
            parentColumn = Profile.ID,
            entityColumn = Stage.PROFILE_ID
    )
    private List<Stage> stages;

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public List<Stage> getStages() {
        return stages;
    }

    public void setStages(List<Stage> stages) {
        Collections.sort(stages);
        this.stages = stages;
    }
}
