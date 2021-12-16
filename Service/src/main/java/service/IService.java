package service;

import domain.Expense;
import domain.ExpenseCategory;
import domain.TotalExpensesDto;
import domain.User;
import domain.UserRequest;
import dto.ExpenseDto;
import dto.MonthlyBudgetDto;
import dto.WishlistItemDto;
import service.exception.ServiceException;
import viewmodel.ExpenseViewModel;
import viewmodel.MonthlyBudgetViewModel;
import viewmodel.WishlistItemViewModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

public interface IService {
    /**
     * Creates an account which is not validated
     * @param email the email to be associated with the account
     * @param firstName user's first name
     * @param lastName user's last name
     * @param dateOfBirth unix time
     * @param password hex string containing the password
     * @return an Optional
     * - empty if successful
     * - containing the userRequest created by the given parameters, otherwise
     */
    Optional<UserRequest> createAccount(String email, String firstName, String lastName, Date dateOfBirth, String password);

    /**
     * Activates the account corresponding to the UserRequest with the given activation token
     * @param activationToken - activation token used to identify UserRequest
     * @return true if the activation is successful and false otherwise
     */
    boolean activateAccount(String activationToken);

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

    MonthlyBudgetViewModel addMonthlyBudget(MonthlyBudgetDto monthlyBudgetDto) throws ServiceException;

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

    /**
     * Method for retrieving the total amount of a user's expenses within a time period, grouped by expense category
     *
     * @param userId, identifier of the user whose expenses are taken into account
     * @param start,  the start of the considered time period
     * @param end,    the end of the considered time period
     * @return a {@code Map} where each key represents an {@code ExpenseCategory} and the value, the total amount of expenses of that category
     */
    Map<ExpenseCategory, Double> getExpenseTotalByCategory(int userId, LocalDateTime start, LocalDateTime end);

    /**
     * Updates a monthly budget
     * @param budgetId id of the budget to be updated
     * @param monthlyBudgetDto a dto object of the monthly budget to be updated
     * @return the viewModel of the updated monthly budget
     * @throws ServiceException if the budget can't be updated
     */
    MonthlyBudgetViewModel updateMonthlyBudget(int budgetId, MonthlyBudgetDto monthlyBudgetDto) throws ServiceException;

    Iterable<MonthlyBudgetViewModel> getMonthlyBudgets(int userId, Date startDate, Date endDate) throws ServiceException;

    ExpenseViewModel updateExpense(ExpenseDto updateExpenseDto, int expenseId) throws ServiceException;

    Iterable<TotalExpensesDto> getTotalExpensesInTime(int userId, String granularity, LocalDate startDate, LocalDate endDate, String category) throws ServiceException;

    /**
     * save a wishlistItem-custom or normal(from search results)
     * @param wishlistItemDto-the object to be saved
     * @return-null if succes,the entity if save in DB fails
     * @throws ServiceException
     */
    WishlistItemViewModel addWishlistItem(WishlistItemDto wishlistItemDto) throws ServiceException;

    Iterable<WishlistItemViewModel> getWishlistItems(int userId);

    /**
     * Method for getting the affordable items in a user's wishlist based on his total savings
     * @param userId - the id of the user
     * @return a list of wishlistItemViewModels
     */
    Iterable<WishlistItemViewModel> getAffordableWishlistItems(int userId);

    ExpenseViewModel purchaseWishlistItem(int wishlistItemId, ExpenseDto expenseDto) throws ServiceException;
}
