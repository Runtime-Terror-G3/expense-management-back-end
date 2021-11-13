package service;

import domain.MonthlyBudget;
import domain.Expense;
import domain.User;
import dto.ExpenseDto;
import dto.MonthlyBudgetDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import repository.IExpenseRepository;
import repository.IMonthlyBudgetRepository;
import repository.IUserRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Objects;
import service.exception.ServiceException;
import viewmodel.ExpenseViewModel;
import viewmodel.MonthlyBudgetViewModel;

import java.util.Optional;

@Component
public class Service implements IService {
    @Autowired
    private final IUserRepository userRepository;
    @Autowired
    private final IExpenseRepository expenseRepository;
    @Autowired
    private final IMonthlyBudgetRepository monthlyBudgetRepository;

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

    @Override
    public MonthlyBudgetViewModel addMonthlyBudget(MonthlyBudgetDto monthlyBudgetDto) throws ServiceException {
        MonthlyBudget monthlyBudget = MonthlyBudget.fromMonthlyBudgetDto(monthlyBudgetDto);

        Optional<MonthlyBudget> savedMonthlyBudget = monthlyBudgetRepository.save(monthlyBudget);

        if (savedMonthlyBudget.isPresent()) {
            throw new ServiceException("An error occurred while saving the monthly budget.");
        }

        return MonthlyBudgetViewModel.fromMonthlyBudget(monthlyBudget);
    }

    @Override
    public Optional<User> createAccount(String email, String firstName, String lastName, Date dateOfBirth, String password) {
        // if the email is already used, another account with the same email cannot be created
        Optional<User> existingUser = userRepository.findByEmail(email);
        if(existingUser.isPresent())
            return existingUser;

        String passwordHash = hashPassword(password);

        User newUser = new User(email, firstName, lastName, dateOfBirth, passwordHash);

        return userRepository.save(newUser);

    }
}
