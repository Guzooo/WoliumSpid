package pl.Guzooo.WoliumSpid.Database;

import android.content.ContentValues;
import android.database.Cursor;

public class Stage extends DatabaseObject{
    public static final String DATABASE_NAME = "Stage";
    public static final String PROFILE_ID = "ProfileId";
    public static final String VOLUME = "Volume";
    public static final String SPEED = "Speed";
    public static final String[] ON_CURSOR = {
            Database.ID,
            PROFILE_ID,
            VOLUME,
            SPEED
    };

    private int profileId;
    private int volume;
    private int speed;

    @Override
    public String[] onCursor() {
        return ON_CURSOR;
    }

    @Override
    public String databaseName() {
        return DATABASE_NAME;
    }

    @Override
    public void setVariablesOfCursor(Cursor cursor) {
        template(cursor.getInt(0),
                cursor.getInt(1),
                cursor.getInt(2),
                cursor.getInt(3));
    }

    @Override
    public void setVariablesEmpty() {
        template(0, 0, 0, 0);
    }

    @Override
    protected ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PROFILE_ID, profileId);
        contentValues.put(VOLUME, volume);
        contentValues.put(SPEED, speed);
        return contentValues;
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

    private void template(int id,
                          int profileId,
                          int volume,
                          int speed){
        this.id = id;
        this.profileId = profileId;
        this.volume = volume;
        this.speed = speed;
    }
}
