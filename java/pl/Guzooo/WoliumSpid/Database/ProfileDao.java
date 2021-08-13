package pl.Guzooo.WoliumSpid.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ProfileDao {

    @Insert
    void insert(Profile profile);

    @Update
    void update(Profile profile, List<Stage> stages);

    @Delete
    void delete(Profile profile, List<Stage> stages);

    @Transaction
    @Query("SELECT * FROM " + Profile.DATABASE_NAME)
    LiveData<List<ProfileWithStages>> getAllProfilesWithStages();

    @Transaction
    @Query("SELECT * FROM " + Profile.DATABASE_NAME + " WHERE " + Profile.ID + " = :id")
    LiveData<ProfileWithStages> getOneProfileWithStages(int id);

    @Transaction
    @Query("SELECT * FROM " + Profile.DATABASE_NAME + " ORDER BY " + Profile.ID + " DESC LIMIT 1")
    LiveData<ProfileWithStages> getLast();
}
