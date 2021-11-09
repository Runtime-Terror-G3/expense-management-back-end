package service;

import domain.User;
import repository.IExpenseRepository;
import repository.IMonthlyBudgetRepository;
import repository.IUserRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Optional;

public class Service implements IService {
    private IUserRepository userRepo;
    private IExpenseRepository expenseRepository;
    private IMonthlyBudgetRepository monthlyBudgetRepository;

    public Service(IUserRepository userRepo, IExpenseRepository expenseRepository,
                   IMonthlyBudgetRepository monthlyBudgetRepository) {

        this.userRepo = userRepo;
        this.expenseRepository = expenseRepository;
        this.monthlyBudgetRepository = monthlyBudgetRepository;
    }

    public Optional<User> login(String email, String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String hash = new String(md.digest((password + "primarily sodium chloride")
                    .getBytes(StandardCharsets.US_ASCII)));

            Optional<User> user = userRepo.findByEmail(email);

            if(user.isPresent() && Objects.equals(user.get().getPasswordHash(), hash)) {
                return user;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }
}
