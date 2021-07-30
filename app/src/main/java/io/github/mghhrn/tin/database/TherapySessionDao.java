package io.github.mghhrn.tin.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import io.github.mghhrn.tin.entity.TherapySession;

@Dao
public interface TherapySessionDao {

    @Insert
    long insert(TherapySession therapySession);

    @Query("SELECT * FROM therapy_session WHERE id = :id")
    TherapySession findById(long id);

    @Update
    int update(TherapySession therapySession);
}
