package com.example.apptuhorasalud.infrastructure.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.apptuhorasalud.infrastructure.entitys.UserEntity;

@Database(entities = {UserEntity.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao usuarioDao();
}
