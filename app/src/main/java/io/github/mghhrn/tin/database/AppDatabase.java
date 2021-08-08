package io.github.mghhrn.tin.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import io.github.mghhrn.tin.entity.TherapySession;

@Database(entities = {TherapySession.class}, version = 2)
@TypeConverters({DateTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract TherapySessionDao therapySessionDao();
}
