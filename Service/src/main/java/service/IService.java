package service;

import domain.Expense;
import domain.ExpenseCategory;
import domain.User;
import dto.ExpenseDto;
import service.exception.ServiceException;
import viewmodel.ExpenseViewModel;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

public interface IService {
    /**
     * Creates an account
     * @param email the email to be associated with the account
     * @param firstName user's first name
     * @param lastName user's last name
     * @param dateOfBirth unix time
     * @param password hex string containing the password
     * @return an Optional
     * - empty if successful
     * - containing the user created by the given parameters, otherwise
     */
    Optional<User> createAccount(String email, String firstName, String lastName, Date dateOfBirth, String password);

    Optional<User> login(String email, String password);

    String generateUserToken(User user);

    Optional<User> getTokenUser(String token);

    /**
        Deletes a monthly budget of a given user
     * @param budgetId id of the budget to be deleted
     * @param userId id of the user who requested the deletion
     * @return a ServiceEmptyResponse with status=200 in case of success
     * a ServiceEmptyResponse with status=403 in case the budget with
     * the given id is not owned by the user with the given userId
     * a ServiceEmptyResponse with status=500 in case the delete operation fails
     */
    ServiceEmptyResponse deleteMonthlyBudget(int budgetId, int userId);

    ExpenseViewModel addExpense(ExpenseDto expenseDto) throws ServiceException;

    /**
     * Get expenses filtered by a date interval
     * @param userId id of the user the expenses belong to
     * @param category category of the expenses
     * @param startDate unix timestamp for the beginning of the interval
     * @param endDate unix timestamp for the end of the interval
     * @return a collection of expenses
     * @throws ServiceException if the parameters are faulty
     */
    Iterable<Expense> getExpenses(int userId, String category, long startDate, long endDate) throws ServiceException;

    /**
     * Method for retrieving the total amount of a user's expenses within a time period, grouped by expense category
     * @param userId, identifier of the user whose expenses are taken into account
     * @param start, the start of the considered time period
     * @param end, the end of the considered time period
     * @return a {@code Map} where each key represents an {@code ExpenseCategory} and the value, the total amount of expenses of that category
     */
    Map<ExpenseCategory, Double> getExpenseTotalByCategory(int userId, LocalDateTime start, LocalDateTime end);
}
