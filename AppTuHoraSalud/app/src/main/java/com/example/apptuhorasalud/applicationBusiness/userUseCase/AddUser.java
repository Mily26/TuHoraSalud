package com.example.apptuhorasalud.applicationBusiness.userUseCase;

import com.example.apptuhorasalud.domain.models.User;
import com.example.apptuhorasalud.domain.repositories.IUserRepository;

public class AddUser {

    private final IUserRepository<User> repository;

    public AddUser(IUserRepository<User> repository){
        this.repository = repository;
    }

    public void execute(User user){
        repository.addUser(user);
    }

}
