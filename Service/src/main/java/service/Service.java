package service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import domain.MonthlyBudget;
import domain.Expense;
import domain.User;
import dto.ExpenseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import repository.IExpenseRepository;
import repository.IMonthlyBudgetRepository;
import repository.IUserRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import service.exception.ServiceException;
import viewmodel.ExpenseViewModel;
import java.util.Optional;

@Component
public class Service implements IService {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IExpenseRepository expenseRepository;
    @Autowired
    private IMonthlyBudgetRepository monthlyBudgetRepository;
    private static long tokenTime = 8 * 60 * 60 * 1000; // 8 hours
    private static Algorithm signingAlgorith = Algorithm.HMAC256("super secret token secret");

    public Service(
            IUserRepository userRepository,
            IExpenseRepository expenseRepository,
            IMonthlyBudgetRepository monthlyBudgetRepository
    ) {
        this.userRepository = userRepository;
        this.expenseRepository = expenseRepository;
        this.monthlyBudgetRepository = monthlyBudgetRepository;
    }

    private static String hashPassword(String password) {
        final String salt = "primarily sodium chloride";

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            return new String(md.digest((password + salt)
                    .getBytes(StandardCharsets.US_ASCII)));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String generateJWT(int id, String firstName, String lastName, long iat, long exp) {
        return JWT.create()
                .withClaim("ID", id)
                .withClaim("first_name", firstName)
                .withClaim("last_name", lastName)
                .withClaim("iat", iat)
                .withClaim("exp", exp)
                .sign(signingAlgorith);
    }

    @Override
    public String generateUserToken(User user) {
        long currentTime = System.currentTimeMillis();

        return generateJWT(user.getId(), user.getFirstName(), user.getLastName(), currentTime,
                currentTime + tokenTime);
    }

    @Override
    public Optional<User> login(String email, String password) {
        String hash = hashPassword(password);

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent() && Objects.equals(user.get().getPasswordHash(), hash)) {
            return user;
        }

        return Optional.empty();
    }

    @Override
    public ServiceEmptyResponse deleteMonthlyBudget(int budgetId, int userId) {
        ServiceEmptyResponse response = new ServiceEmptyResponse(200, "");

        Optional<MonthlyBudget> budgetToDelete = monthlyBudgetRepository.findOne(budgetId);

        if (budgetToDelete.isPresent()) {
            if (budgetToDelete.get().getUser().getId() != userId) {
                response.setStatus(403);
                response.setErrorMessage("Not allowed to delete this resource");
                return response;
            }

            Optional<MonthlyBudget> deletedBudget = monthlyBudgetRepository.delete(budgetId);

            if (!deletedBudget.isPresent()) {
                response.setStatus(500);
                response.setErrorMessage("Internal Server Error");
            }
        }
        return response;
    }

    @Override
    public ExpenseViewModel addExpense(ExpenseDto expenseDto) throws ServiceException {
        Expense expense = Expense.fromExpenseDto(expenseDto);

        Optional<Expense> savedExpense = expenseRepository.save(expense);

        if (savedExpense.isPresent()) {
            throw new ServiceException("An error occurred while saving the expense.");
        }

        return ExpenseViewModel.fromExpense(expense);
    }
}
