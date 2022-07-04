package pl.Guzooo.WoliumSpid.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static pl.Guzooo.WoliumSpid.Database.Stage.DATABASE_NAME;

@Entity(tableName = DATABASE_NAME)
public class Stage implements Comparable<Stage>{
    public static final String DATABASE_NAME = "stages";
    public static final String ID = "_id";
    public static final String PROFILE_ID = "profile_id";
    public static final String VOLUME = "volume";
    public static final String SPEED_NEXT = "speed";
    public static final String SPEED_BACK = "speed2"; //Added in db version 2.
    public static final String ORDER = "orderr";

    public static final float DEFAULT_SPEED_BACK = -1;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    private int id;
    @ColumnInfo(name = PROFILE_ID)
    private int profileId;
    @ColumnInfo(name = VOLUME)
    private int volume;
    @ColumnInfo(name = SPEED_NEXT)
    private float speedNext;
    @ColumnInfo(name = SPEED_BACK, defaultValue = DEFAULT_SPEED_BACK+"")
    private float speedBack;
    @ColumnInfo(name = ORDER)
    private int order;

    @Ignore
    private float realSpeedBack;
    @Ignore
    private boolean active = false;
    @Ignore
    private boolean skipNext = false;
    @Ignore
    private boolean skipBack = false;
    @Ignore
    private boolean last = false;

    public Stage duplicate(){
        Stage clone = new Stage();
        clone.setId(id);
        clone.setProfileId(profileId);
        clone.setVolume(volume);
        clone.setSpeedNext(speedNext);
        clone.setSpeedBack(speedBack);
        clone.setOrder(order);
        return clone;
    }

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

    public float getSpeedNext() {
        return speedNext;
    }

    public void setSpeedNext(float speed) {
        this.speedNext = speed;
    }

    public float getSpeedBack() {
        return speedBack;
    }

    public void setSpeedBack(float speed) {
        speed = addSomeToZero(speed);
        this.speedBack = speed;
        if(speed != DEFAULT_SPEED_BACK)
            realSpeedBack = speed;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public float getRealSpeedBack() {
        return realSpeedBack;
    }

    public void setRealSpeedBack(Stage nextStage) {
        if(speedBack == DEFAULT_SPEED_BACK)
            realSpeedBack = addSomeToZero(nextStage.speedNext);
        else
            realSpeedBack = speedBack;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isSkipNext() {
        return skipNext;
    }

    public void setSkipNext(boolean skipNext) {
        this.skipNext = skipNext;
    }

    public boolean isSkipBack(){
        return skipBack;
    }

    public void setSkipBack(boolean skipBack){
        this.skipBack = skipBack;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last){
        this.last = last;
    }

    @Override
    public int compareTo(Stage stage) {
        return Integer.compare(order, stage.order);
    }

    private float addSomeToZero(float speed){
        if(speed == 0)
            return 0.01f;
        return speed;
    }
}