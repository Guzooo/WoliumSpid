package pl.Guzooo.WoliumSpid.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;

import pl.Guzooo.Database.DatabaseUtils;
import pl.Guzooo.WoliumSpid.R;

public class Profile extends DatabaseObject{
    public static final String DATABASE_NAME = "Profile";
    public static final String NAME = "Name";
    public static final String[] ON_CURSOR = {
            Database.ID,
            NAME
    };

    private String name;
    private ArrayList<Stage> stages = new ArrayList<>();

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
                cursor.getString(1));
    }

    @Override
    public void setVariablesEmpty() {
        template(0,
                "");
    }

    @Override
    protected ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);
        return contentValues;
    }

    public void loadStages(Context context){
        try{
            SQLiteDatabase database = getDatabase(context).getReadableDatabase();
            Cursor cursor = database.query(Stage.DATABASE_NAME,
                    Stage.ON_CURSOR,
                    Stage.PROFILE_ID + " = ?",
                    new String[]{Integer.toString(id)},
                    null, null,
                    Stage.ORDER);
            if(cursor.moveToFirst())
                do {
                   Stage stage = new Stage();
                   stage.setVariablesOfCursor(cursor);
                   addStage(stage);
                } while (cursor.moveToNext());
            cursor.close();
            database.close();
        } catch (SQLiteException e){
            DatabaseUtils.errorToast(context);
        }
    }

    public String getName(Context context) {
        if(name.isEmpty())
            return context.getString(R.string.profile, id);
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addStage(Stage stage){
        stages.add(stage);
    }//TODO: dorobić wiecej metod ze stage'ami

    public Stage getStage(int stageNumber){
        return stages.get(stageNumber);
    }

    private void template(int id,
                          String name){
        this.id = id;
        this.name = name;
    }
}
