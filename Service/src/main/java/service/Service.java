package service;

import domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import repository.IExpenseRepository;
import repository.IMonthlyBudgetRepository;
import repository.IUserRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Optional;

@Component
public class Service implements IService {
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

    public Optional<User> login(String email, String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String hash = new String(md.digest((password + "primarily sodium chloride")
                    .getBytes(StandardCharsets.US_ASCII)));

            Optional<User> user = userRepository.findByEmail(email);

            if (user.isPresent() && Objects.equals(user.get().getPasswordHash(), hash)) {
                return user;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public Optional<User> testAddUser(User user) {
        return userRepository.save(user);
    }
}