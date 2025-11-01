package com.example.apptuhorasalud.infrastructure.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.apptuhorasalud.infrastructure.entitys.MedicineEntity;
import com.example.apptuhorasalud.infrastructure.entitys.UserEntity;

@Database(entities = {UserEntity.class, MedicineEntity.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao usuarioDao();
    public abstract MedicineDao medicineDao();
}
