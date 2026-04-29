package com.example.apptuhorasalud.infrastructure.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.apptuhorasalud.infrastructure.entitys.AlarmEntity;

import java.util.List;

@Dao
public interface AlarmDao {
    @Insert
    long insert(AlarmEntity alarmEntity);

    @Query("SELECT * FROM alarms WHERE userId = :userId AND isDeleted = 0")
    List<AlarmEntity> getAlarmsByUserId(int userId);
}
