package pl.Guzooo.WoliumSpid.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class Database extends pl.Guzooo.Database.Database {

    private static final String DATABASE_NAME = "woliumspid";
    private static final int DATABASE_VERSION = 1;

    Database (Context context){
        super(DATABASE_NAME, DATABASE_VERSION, context);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        super.onCreate(database);
        createTableProfile();
        createTableStage();
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        super.onUpgrade(database, oldVersion, newVersion);
    }

    private void createTableProfile(){
        getDatabase().execSQL("CREATE TABLE " + Profile.DATABASE_NAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + Profile.NAME + " TEXT)");
    }

    private void createTableStage(){
        getDatabase().execSQL("CREATE TABLE " + Stage.DATABASE_NAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + Stage.PROFILE_ID + " INTEGER,"
                        + Stage.VOLUME + " INTEGER,"
                        + Stage.SPEED + " INTEGER)");
    }
}
