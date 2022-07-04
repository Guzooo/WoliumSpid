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
        Stage stage1 = new Stage();
        stage1.setProfileId(1);
        stage1.setOrder(1);
        stage1.setVolume(0);
        stage1.setSpeedNext(0);
        stage1.setSpeedBack(0.1f);
        instance.stageDao().insert(stage1);
        Stage stage2 = new Stage();
        stage2.setProfileId(1);
        stage2.setOrder(2);
        stage2.setVolume(2);
        stage2.setSpeedNext(2);
        stage2.setSpeedBack(8);
        instance.stageDao().insert(stage2);
        Stage stage3 = new Stage();
        stage3.setProfileId(1);
        stage3.setOrder(3);
        stage3.setVolume(10);
        stage3.setSpeedNext(8);
        instance.stageDao().insert(stage3);
    }

    private static void createBicycleProfile(Context context){
        Profile profile = new Profile();
        profile.setId(2);
        profile.setName(context.getString(R.string.profile_bicycle));
        instance.profileDao().insert(profile);
        Stage stage1 = new Stage();
        stage1.setProfileId(2);
        stage1.setOrder(1);
        stage1.setVolume(0);
        stage1.setSpeedNext(0);
        stage1.setSpeedBack(3);
        instance.stageDao().insert(stage1);
        Stage stage2 = new Stage();
        stage2.setProfileId(2);
        stage2.setOrder(2);
        stage2.setVolume(7);
        stage2.setSpeedNext(5);
        stage2.setSpeedBack(8);
        instance.stageDao().insert(stage2);
        Stage stage3 = new Stage();
        stage3.setProfileId(2);
        stage3.setOrder(3);
        stage3.setVolume(12);
        stage3.setSpeedNext(10);
        stage3.setSpeedBack(19);
        instance.stageDao().insert(stage3);
        Stage stage4 = new Stage();
        stage4.setProfileId(2);
        stage4.setOrder(4);
        stage4.setVolume(15);
        stage4.setSpeedNext(21);
        instance.stageDao().insert(stage4);
    }

    private static void createCarProfile(Context context){
        Profile profile = new Profile();
        profile.setId(3);
        profile.setName(context.getString(R.string.profile_car));
        instance.profileDao().insert(profile);
        Stage stage0 = new Stage();
        stage0.setProfileId(3);
        stage0.setOrder(1);
        stage0.setVolume(4);
        stage0.setSpeedNext(0);
        stage0.setSpeedBack(0.1f);
        instance.stageDao().insert(stage0);
        Stage stage1 = new Stage();
        stage1.setProfileId(3);
        stage1.setOrder(2);
        stage1.setVolume(15);
        stage1.setSpeedNext(10);
        stage1.setSpeedBack(50.5f);
        instance.stageDao().insert(stage1);
        Stage stage2 = new Stage();
        stage2.setProfileId(3);
        stage2.setOrder(3);
        stage2.setVolume(8);
        stage2.setSpeedNext(51);
        stage2.setSpeedBack(50.5f);
        instance.stageDao().insert(stage2);
        Stage stage3 = new Stage();
        stage3.setProfileId(3);
        stage3.setOrder(4);
        stage3.setVolume(15);
        stage3.setSpeedNext(56);
        stage3.setSpeedBack(90.5f);
        instance.stageDao().insert(stage3);
        Stage stage4 = new Stage();
        stage4.setProfileId(3);
        stage4.setOrder(5);
        stage4.setVolume(8);
        stage4.setSpeedNext(91);
        stage4.setSpeedBack(90.5f);
        instance.stageDao().insert(stage4);
        Stage stage5 = new Stage();
        stage5.setProfileId(3);
        stage5.setOrder(6);
        stage5.setVolume(15);
        stage5.setSpeedNext(96);
        stage5.setSpeedBack(140.5f);
        instance.stageDao().insert(stage5);
        Stage stage6 = new Stage();
        stage6.setProfileId(3);
        stage6.setOrder(7);
        stage6.setVolume(4);
        stage6.setSpeedNext(140.5f);
        instance.stageDao().insert(stage6);
    }
}