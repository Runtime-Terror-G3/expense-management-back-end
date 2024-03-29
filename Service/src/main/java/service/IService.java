package service;

import domain.*;
import dto.ExpenseDto;
import dto.MonthlyBudgetDto;
import dto.WishlistItemDto;
import service.exception.AuthorizationException;
import service.exception.ServiceException;
import viewmodel.ExpenseViewModel;
import viewmodel.MonthlyBudgetViewModel;
import viewmodel.WishlistItemViewModel;

import java.io.IOException;
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

    /**
     * Login into the app
     * @param email the email of the user
     * @param password the password of the user
     * @return the logged in User if the login was successful or an empty Optional otherwise
     */
    Optional<User> login(String email, String password);

    /**
     * Generate the JWT for the given user
     * @param user the User to generate the token for
     * @return the generated JWT - String
     */
    String generateUserToken(User user);

    /**
     * Extract the user from the token
     * @param token the token to extract the User from
     * @return an Optional, which contains the extracted User if the operation was successful,
     *          otherwise it is empty
     */
    Optional<User> getTokenUser(String token);

    /**
     * Deletes a monthly budget of a given user
     * @param budgetId id of the budget to be deleted
     * @param userId id of the user who requested the deletion
     * @return a ServiceEmptyResponse with status=200 in case of success
     * a ServiceEmptyResponse with status=403 in case the budget with
     * the given id is not owned by the user with the given userId
     * a ServiceEmptyResponse with status=500 in case the delete operation fails
     */
    ServiceEmptyResponse deleteMonthlyBudget(int budgetId, int userId);

    /**
     * Add an expense
     * @param expenseDto an ExpenseDto corresponding to the expense to be added
     * @return an ExpenseViewModel corresponding to the expense, if it was added successfully
     * @throws ServiceException if the expense could not be added
     */
    ExpenseViewModel addExpense(ExpenseDto expenseDto) throws ServiceException;

    /**
     * Add a monthly budget
     * @param monthlyBudgetDto a MonthlyBudgetDto corresponding to the monthly budget to be added
     * @return a MonthlyBudgetViewModel corresponding to the monthly budget, if it was added successfully
     * @throws ServiceException if the monthly budget could not be added
     */
    MonthlyBudgetViewModel addMonthlyBudget(MonthlyBudgetDto monthlyBudgetDto) throws ServiceException;

    /**
     * delete an expense
     * @param expenseId-id of the expense
     * @param userId-id of the user who make the request
     * @return - a ServiceEmptyResponse with status:
     * 200-succes
     * 403-the user who make the request isn't the user with this expense
     * 500-internal server error
     * 404-expense not found
     * @throws ServiceException if something goes wrong on the server
     */
    ExpenseViewModel deleteExpense(int expenseId, int userId) throws ServiceException;

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

    /**
     * Get the monthly budgets for a user during a period of time
     * @param userId the id of the user
     * @param startDate the beginning of the time period
     * @param endDate the end of the time period
     * @return a collection of view models for the monthly budgets available in the given time interval
     * @throws ServiceException if the endDate is greater than the startDate
     */
    Iterable<MonthlyBudgetViewModel> getMonthlyBudgets(int userId, Date startDate, Date endDate) throws ServiceException;

    /**
     * updates an expense
     * @param updateExpenseDto entity containing the data used to update the expense
     * @param expenseId the id of the expense to be updated
     * @return the viewModel of the updated expense
     * @throws ServiceException if the expenseId is invalid or an error occurred while updating the expense
     * @throws AuthorizationException if the user isn't authorized to access the expense with expenseId
     */
    ExpenseViewModel updateExpense(ExpenseDto updateExpenseDto, int expenseId) throws ServiceException, AuthorizationException;

    /**
     * gets an iterable with totalExpensesDto for the specified period of time, for the specified category and for the specified granularity
     * @param userId the id of the user
     * @param granularity granularity of the filtered results
     * @param startDate the start date from which we want the results
     * @param endDate the end date from which we want the results
     * @param category the category for which we want the results
     * @return an iterable with the asked totalExpensesDto
     * @throws ServiceException if the start date is after the end date or the granularity isn't year, month or day
     */
    Iterable<TotalExpensesDto> getTotalExpensesInTime(int userId, String granularity, LocalDate startDate, LocalDate endDate, String category) throws ServiceException;

    /**
     * save a wishlistItem-custom or normal(from search results)
     * @param wishlistItemDto-the object to be saved
     * @return null if succes,the entity if save in DB fails
     * @throws ServiceException if the wishlist item could not be saved
     */
    WishlistItemViewModel addWishlistItem(WishlistItemDto wishlistItemDto) throws ServiceException;

    /**
     * gets all of the items from a user's wishlist, along with a flag that shows whether each item is affordable based on the user's savings
     * @param userId - the id of the user
     * @return a list of wishlistItemViewModels
     * @throws IOException if the parser couldn't compute an item's price
     */
    Iterable<WishlistItemViewModel> getWishlistItems(int userId) throws IOException;

    /**
     * deletes a wishlist item and adds an expense instead
     * @param wishlistItemId - id of the wishlist item to be deleted
     * @param expenseDto - expense to be added
     * @return - the added expense
     * @throws ServiceException when the wishlist item does not exist or an error occurred while saving the expense or deleting the wishlist item
     * @throws AuthorizationException when the user isn't authorized to access that wishlist item
     */
    ExpenseViewModel purchaseWishlistItem(int wishlistItemId, ExpenseDto expenseDto) throws ServiceException, AuthorizationException;

    /**
     * Retrieve a collection of products from a specific vendor, filtered by a keyword
     * @param keyword the keyword by which the product search is done
     * @param vendor the desired vendor
     * @return a collection of WishlistItemViewModel, each representing a product
     * @throws ServiceException if something is wrong with the parameters
     * @throws IOException if something went wrong during the retrieval of products from the specified vendor
     */
    Iterable<WishlistItemViewModel> findProductsByKeywordAndVendor(String keyword, String vendor) throws ServiceException, IOException;

    /**
     * delete a wishlistItem
     * @param wishlistItemId-id of the item
     * @param userId-id of the user who make the request
     * @return the deleted item
     * @throws ServiceException when the wishlist item does not exist or an error occurred while deleting the wishlist item
     * @throws AuthorizationException when the user isn't authorized to access that wishlist item
     */
    WishlistItemViewModel deleteWishlistItem(int wishlistItemId, int userId)throws ServiceException,AuthorizationException;
}
