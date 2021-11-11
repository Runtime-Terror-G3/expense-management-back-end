package service;

import service.exception.ServiceException;

public interface IService {

    /**
     * Deletes a monthly budget of a given user
     * @param budgetId id of the budget to be deleted
     * @param userId id of the user who requested the deletion
     * @return true in case os success and false otherwise
     * @throws ServiceException if a budget with the given budgetId doesn't exist
     *                          or the userId is not the id of the user who really owns the budget
     */
    Boolean deleteMonthlyBudget(int budgetId, int userId) throws ServiceException;
}
