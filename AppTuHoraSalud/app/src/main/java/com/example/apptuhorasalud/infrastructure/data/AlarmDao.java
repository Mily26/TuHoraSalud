package com.example.apptuhorasalud.infrastructure.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.apptuhorasalud.infrastructure.entitys.AlarmEntity;

import java.util.List;

@Dao
public interface AlarmDao {
    @Insert
    long insert(AlarmEntity alarmEntity);

    @Update
    void update(AlarmEntity alarmEntity);

    @Query("UPDATE alarms SET isActive = :isActive WHERE id = :alarmId")
    void setActive(int alarmId, boolean isActive);

    @Query("SELECT * FROM alarms WHERE userId = :userId AND isDeleted = 0")
    List<AlarmEntity> getAlarmsByUserId(int userId);
}
