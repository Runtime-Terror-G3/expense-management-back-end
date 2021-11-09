package service;

import domain.User;

import java.util.Optional;

public interface IService {
    public Optional<User> login(String email, String password);
}
