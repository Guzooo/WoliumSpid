package pl.Guzooo.WoliumSpid.Database;

import android.content.Context;

import pl.Guzooo.Database.Database;

public abstract class DatabaseObject extends pl.Guzooo.Database.DatabaseObject {

    @Override
    protected Database getDatabase(Context context) {
        return new pl.Guzooo.WoliumSpid.Database.Database(context);
    }
}
