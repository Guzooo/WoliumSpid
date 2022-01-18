package pl.Guzooo.WoliumSpid.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.Guzooo.WoliumSpid.R;

@Database(entities = {Profile.class, Stage.class}, version = 1)
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
        stage1.setSpeed(0);
        instance.stageDao().insert(stage1);
        Stage stage2 = new Stage();
        stage2.setProfileId(1);
        stage2.setOrder(2);
        stage2.setVolume(2);
        stage2.setSpeed(2);
        instance.stageDao().insert(stage2);
        Stage stage3 = new Stage();
        stage3.setProfileId(1);
        stage3.setOrder(3);
        stage3.setVolume(10);
        stage3.setSpeed(8);
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
        stage1.setSpeed(0);
        instance.stageDao().insert(stage1);
        Stage stage2 = new Stage();
        stage2.setProfileId(2);
        stage2.setOrder(2);
        stage2.setVolume(7);
        stage2.setSpeed(5);
        instance.stageDao().insert(stage2);
        Stage stage3 = new Stage();
        stage3.setProfileId(2);
        stage3.setOrder(3);
        stage3.setVolume(12);
        stage3.setSpeed(10);
        instance.stageDao().insert(stage3);
        Stage stage4 = new Stage();
        stage4.setProfileId(2);
        stage4.setOrder(4);
        stage4.setVolume(15);
        stage4.setSpeed(21);
        instance.stageDao().insert(stage4);
    }

    private static void createCarProfile(Context context){
        Profile profile = new Profile();
        profile.setId(3);
        profile.setName(context.getString(R.string.profile_car));
        instance.profileDao().insert(profile);
        Stage stage1 = new Stage();
        stage1.setProfileId(3);
        stage1.setOrder(1);
        stage1.setVolume(15);
        stage1.setSpeed(0);
        instance.stageDao().insert(stage1);
        Stage stage2 = new Stage();
        stage2.setProfileId(3);
        stage2.setOrder(2);
        stage2.setVolume(8);
        stage2.setSpeed(51);
        instance.stageDao().insert(stage2);
        Stage stage3 = new Stage();
        stage3.setProfileId(3);
        stage3.setOrder(3);
        stage3.setVolume(15);
        stage3.setSpeed(56);
        instance.stageDao().insert(stage3);
        Stage stage4 = new Stage();
        stage4.setProfileId(3);
        stage4.setOrder(4);
        stage4.setVolume(8);
        stage4.setSpeed(91);
        instance.stageDao().insert(stage4);
        Stage stage5 = new Stage();
        stage5.setProfileId(3);
        stage5.setOrder(5);
        stage5.setVolume(15);
        stage5.setSpeed(96);
        instance.stageDao().insert(stage5);
        Stage stage6 = new Stage();
        stage6.setProfileId(3);
        stage6.setOrder(6);
        stage6.setVolume(4);
        stage6.setSpeed(140.5f);
        instance.stageDao().insert(stage6);

    }
}
