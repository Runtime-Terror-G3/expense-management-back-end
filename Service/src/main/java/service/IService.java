package service;

import domain.Expense;
import domain.User;
import dto.ExpenseDto;
import service.exception.ServiceException;
import viewmodel.ExpenseViewModel;

import java.util.Date;
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
     * delete an expense
     * @param expenseId-id of the expense
     * @param userId-id of the user who make the request
     * @return-a ServiceEmptyResponse with status:
     * 200-succes
     * 403-the user who make the request isn't the user with this expense
     * 500-internal server error
     * 404-expense not found
     */
    ExpenseViewModel deleteExpense(int expenseId, int userId) throws ServiceException;
/*
     * Get expenses filtered by a date interval
     * @param userId id of the user the expenses belong to
     * @param category category of the expenses
     * @param startDate unix timestamp for the beginning of the interval
     * @param endDate unix timestamp for the end of the interval
     * @return a collection of expenses
     * @throws ServiceException if the parameters are faulty
     */
    Iterable<Expense> getExpenses(int userId, String category, long startDate, long endDate) throws ServiceException;

}
