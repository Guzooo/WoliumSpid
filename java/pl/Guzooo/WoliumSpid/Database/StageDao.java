package pl.Guzooo.WoliumSpid.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StageDao {

    @Insert
    void insert(Stage stage);

    @Update
    void update(Stage stage);

    @Update
    void updateAll(List<Stage> stages);

    @Delete
    void delete(Stage stage);

    @Query("SELECT * FROM " + Stage.DATABASE_NAME + " WHERE " + Stage.PROFILE_ID + " = :profileId AND " + Stage.ORDER + " >= :orderMin")
    List<Stage> getStagesFromOneProfileWithOrderGreaterOrEqualsThan(int profileId, int orderMin);

    @Query("SELECT * FROM " + Stage.DATABASE_NAME + " WHERE " + Stage.PROFILE_ID + " = :profileId AND " + Stage.ORDER + " >= :orderMin AND " + Stage.ORDER + " <= :orderMax")
    List<Stage> getStagesFromOneProfileWithOrderBetween(int profileId, int orderMin, int orderMax);
}
