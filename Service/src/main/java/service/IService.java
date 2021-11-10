package service;

import domain.User;

import java.util.Optional;

public interface IService {
    Optional<User> testAddUser(User user);
}
