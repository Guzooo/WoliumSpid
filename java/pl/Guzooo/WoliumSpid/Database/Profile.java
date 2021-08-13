package pl.Guzooo.WoliumSpid.Database;

import android.content.Context;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import pl.Guzooo.WoliumSpid.R;
import static pl.Guzooo.WoliumSpid.Database.Profile.DATABASE_NAME;

@Entity(tableName = DATABASE_NAME)
public class Profile{
    public static final String DATABASE_NAME = "profiles";
    public static final String ID = "_id";
    public static final String NAME = "name";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    private int id;
    @ColumnInfo(name = NAME)
    private String name = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public String getName(Context context) {
        if(name.isEmpty())
            return context.getString(R.string.profile, id);
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
