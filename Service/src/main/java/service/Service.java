package service;

import domain.User;
import repository.IUserRepository;

import java.util.Date;

public class Service implements IService{

    private final IUserRepository userRepository;
    public Service(IUserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public void signUp(String email, String firstName, String lastName, Date dateOfBirth, String passwordHash) {

        User user = new User(email, firstName, lastName, dateOfBirth, passwordHash);

        userRepository.save(user);
    }
}
