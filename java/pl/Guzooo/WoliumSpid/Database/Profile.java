package pl.Guzooo.WoliumSpid.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import pl.Guzooo.WoliumSpid.R;

public class Profile extends DatabaseObject{
    public static final String DATABASE_NAME = "Profile";
    public static final String NAME = "Name";
    public static final String[] ON_CURSOR = {
            Database.ID,
            NAME
    };

    private String name;

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

    public String getName(Context context) {
        if(name.isEmpty())
            return context.getString(R.string.profile, id);
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private void template(int id,
                          String name){
        this.id = id;
        this.name = name;
    }
}
