package pl.Guzooo.WoliumSpid.Database;

import android.content.Context;

import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.AutoMigrationSpec;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.Guzooo.WoliumSpid.R;

@Database(version = 2, entities = {Profile.class, Stage.class},
            autoMigrations = {
                @AutoMigration(from = 1, to = 2)
            }
        )
public abstract class WSDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "woliumspid";
    private static final int NUMBER_OF_THREADS = 4;

    private static WSDatabase instance;
    private static ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract ProfileDao profileDao();
    public abstract StageDao stageDao();

    public static synchronized WSDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    WSDatabase.class, DATABASE_NAME)
                    .addCallback(getCallback(context))
                    .build();
        }
        return instance;
    }

    public static ExecutorService getExecutor(){
        return executor;
    }

    private static RoomDatabase.Callback getCallback(Context context){
        return new RoomDatabase.Callback(){
            @Override
            public void onCreate(SupportSQLiteDatabase db) {
                super.onCreate(db);
                getExecutor().execute(() -> {
                    createRunProfile(context);
                    createBicycleProfile(context);
                    createCarProfile(context);
                }); }
        };
    }

    private static void createRunProfile(Context context){
        Profile profile = new Profile();
        profile.setId(1);
        profile.setName(context.getString(R.string.profile_run));
        instance.profileDao().insert(profile);
        Stage stage = new Stage();
        stage.setProfileId(1);
        stage.setOrder(1);
        stage.setVolume(0);
        stage.setSpeedNext(0);
        stage.setSpeedBack(0.1f);
        instance.stageDao().insert(stage);
        stage.setProfileId(1);
        stage.setOrder(2);
        stage.setVolume(2);
        stage.setSpeedNext(2);
        stage.setSpeedBack(8);
        instance.stageDao().insert(stage);
        stage.setProfileId(1);
        stage.setOrder(3);
        stage.setVolume(10);
        stage.setSpeedNext(8);
        stage.setSpeedBack(-1);
        instance.stageDao().insert(stage);
    }

    private static void createBicycleProfile(Context context){
        Profile profile = new Profile();
        profile.setId(2);
        profile.setName(context.getString(R.string.profile_bicycle));
        instance.profileDao().insert(profile);
        Stage stage = new Stage();
        stage.setProfileId(2);
        stage.setOrder(1);
        stage.setVolume(0);
        stage.setSpeedNext(0);
        stage.setSpeedBack(3);
        instance.stageDao().insert(stage);
        stage.setProfileId(2);
        stage.setOrder(2);
        stage.setVolume(7);
        stage.setSpeedNext(5);
        stage.setSpeedBack(8);
        instance.stageDao().insert(stage);
        stage.setProfileId(2);
        stage.setOrder(3);
        stage.setVolume(12);
        stage.setSpeedNext(10);
        stage.setSpeedBack(19);
        instance.stageDao().insert(stage);
        stage.setProfileId(2);
        stage.setOrder(4);
        stage.setVolume(15);
        stage.setSpeedNext(21);
        stage.setSpeedBack(-1);
        instance.stageDao().insert(stage);
    }

    private static void createCarProfile(Context context){
        Profile profile = new Profile();
        profile.setId(3);
        profile.setName(context.getString(R.string.profile_car));
        instance.profileDao().insert(profile);
        Stage stage = new Stage();
        stage.setProfileId(3);
        stage.setOrder(1);
        stage.setVolume(4);
        stage.setSpeedNext(0);
        stage.setSpeedBack(0.1f);
        instance.stageDao().insert(stage);
        stage.setProfileId(3);
        stage.setOrder(2);
        stage.setVolume(15);
        stage.setSpeedNext(10);
        stage.setSpeedBack(50.5f);
        instance.stageDao().insert(stage);
        stage.setProfileId(3);
        stage.setOrder(3);
        stage.setVolume(8);
        stage.setSpeedNext(51);
        stage.setSpeedBack(50.5f);
        instance.stageDao().insert(stage);
        stage.setProfileId(3);
        stage.setOrder(4);
        stage.setVolume(15);
        stage.setSpeedNext(56);
        stage.setSpeedBack(90.5f);
        instance.stageDao().insert(stage);
        stage.setProfileId(3);
        stage.setOrder(5);
        stage.setVolume(8);
        stage.setSpeedNext(91);
        stage.setSpeedBack(90.5f);
        instance.stageDao().insert(stage);
        stage.setProfileId(3);
        stage.setOrder(6);
        stage.setVolume(15);
        stage.setSpeedNext(96);
        stage.setSpeedBack(140.5f);
        instance.stageDao().insert(stage);
        stage.setProfileId(3);
        stage.setOrder(7);
        stage.setVolume(4);
        stage.setSpeedNext(140.5f);
        stage.setSpeedBack(-1);
        instance.stageDao().insert(stage);
    }
}