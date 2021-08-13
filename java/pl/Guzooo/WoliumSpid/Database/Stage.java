package pl.Guzooo.WoliumSpid.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import static pl.Guzooo.WoliumSpid.Database.Stage.DATABASE_NAME;

@Entity(tableName = DATABASE_NAME)
public class Stage implements Comparable{
    public static final String DATABASE_NAME = "stages";
    public static final String ID = "_id";
    public static final String PROFILE_ID = "profile_id";
    public static final String VOLUME = "volume";
    public static final String SPEED = "speed";
    public static final String ORDER = "orderr";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    private int id;
    @ColumnInfo(name = PROFILE_ID)
    private int profileId;
    @ColumnInfo(name = VOLUME)
    private int volume;
    @ColumnInfo(name = SPEED)
    private int speed;
    @ColumnInfo(name = ORDER)
    private int order;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int compareTo(Object o) {
        Stage otherStage = (Stage) o;
        return Integer.compare(order, otherStage.order);
    }
}