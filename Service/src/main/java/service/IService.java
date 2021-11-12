package service;

import domain.User;
import dto.ExpenseDto;
import service.exception.ServiceException;
import viewmodel.ExpenseViewModel;

import java.util.Date;
import java.util.Optional;

public interface IService {
    Optional<User> createAccount(String email, String firstName, String lastName, Date dateOfBirth, String password);

    Optional<User> login(String email, String password);

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
}
