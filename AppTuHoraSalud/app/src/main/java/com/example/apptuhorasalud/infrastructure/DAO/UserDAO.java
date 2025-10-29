package com.example.apptuhorasalud.infrastructure.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.apptuhorasalud.infrastructure.entitys.UserEntity;

import java.util.List;

public class UserDAO {
    @Dao
    public interface UserDao {
        @Insert
        long insert(UserEntity user);

        @Query("SELECT * FROM users")
        List<UserEntity> getAll();

        @Query("SELECT * FROM users WHERE id = :id")
        UserEntity getById(int id);
    }
}
