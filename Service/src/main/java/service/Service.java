package service;

import domain.User;
import netscape.javascript.JSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import repository.IExpenseRepository;
import repository.IMonthlyBudgetRepository;
import repository.IUserRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Optional;

@Component
public class Service implements IService{

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IExpenseRepository expenseRepository;
    @Autowired
    private IMonthlyBudgetRepository monthlyBudgetRepository;

    public Service(
            IUserRepository userRepository,
            IExpenseRepository expenseRepository,
            IMonthlyBudgetRepository monthlyBudgetRepository
    ) {
        this.userRepository = userRepository;
        this.expenseRepository = expenseRepository;
        this.monthlyBudgetRepository = monthlyBudgetRepository;
    }

    @Override
    public Optional<User> testAddUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> createAccount(String email, String firstName, String lastName, Date dateOfBirth, String password) {
        try {
            // if the email is already used, another account with the same email cannot be created
            Optional<User> existingUser = userRepository.findByEmail(email);
            if(existingUser.isPresent())
                return existingUser;

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String passwordHash = new String(md.digest((password + "primarily sodium chloride")
                    .getBytes(StandardCharsets.US_ASCII)));

            User newUser = new User(email, firstName, lastName, dateOfBirth, passwordHash);

            Optional<User> user = userRepository.save(newUser);
            if(user.isPresent())
                return user;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }
}
