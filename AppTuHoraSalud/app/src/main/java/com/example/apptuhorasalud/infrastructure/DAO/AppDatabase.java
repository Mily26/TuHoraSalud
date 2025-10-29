package com.example.apptuhorasalud.infrastructure.DAO;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.apptuhorasalud.infrastructure.DAO.UserDAO;
import com.example.apptuhorasalud.infrastructure.entitys.UserEntity;

@Database(entities = {UserEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDAO.UserDao userDao();
}