package io.github.mghhrn.tin.database;

import android.content.Context;

import androidx.room.Room;

public class AppDatabaseSingleton {

    private static AppDatabase database;

    private AppDatabaseSingleton() {}

    public static synchronized AppDatabase getInstance(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context, AppDatabase.class, "tin-database")
                    .allowMainThreadQueries()
                    .build();
        }
        return database;
    }
}
