package service;

import java.util.Date;

public interface IService {
    /**
     * Sign up a user
     * @param email
     * @param firstName
     * @param lastName
     * @param dateOfBirth
     * @param passwordHash
     * @throws
     */
    void signUp(String email, String firstName, String lastName, Date dateOfBirth, String passwordHash);
}
