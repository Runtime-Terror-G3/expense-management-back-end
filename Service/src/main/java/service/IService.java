package service;

import domain.User;

import java.util.Date;
import java.util.Optional;

public interface IService {

    Optional<User> createAccount(String email, String firstName, String lastName, Date dateOfBirth, String password);

    Optional<User> testAddUser(User user);
}
