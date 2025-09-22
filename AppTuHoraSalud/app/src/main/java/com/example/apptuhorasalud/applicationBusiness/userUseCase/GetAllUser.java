package com.example.apptuhorasalud.applicationBusiness.userUseCase;

import com.example.apptuhorasalud.domain.models.User;
import com.example.apptuhorasalud.domain.repositories.IUserRepository;

import java.util.List;

public class GetAllUser {
    private final IUserRepository<User> repository;

    public GetAllUser(IUserRepository<User> repository){
        this.repository = repository;
    }

    public List<User> execute(){
        return repository.getAllUser();
    }
}
