package com.example.apptuhorasalud.domain.repositories;

import java.util.List;

public interface IUserRepository<T> {
    void addUser (T model);
    List<T> getAllUser();
    T getByIdUser();
    void deleteUser(int id);
    void updateUser(T model);
}
