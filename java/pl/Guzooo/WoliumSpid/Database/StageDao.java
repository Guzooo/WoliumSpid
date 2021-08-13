package pl.Guzooo.WoliumSpid.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

@Dao
public interface StageDao {

    @Insert
    void insert(Stage stage);

    @Delete
    void delete(Stage stage);
}
