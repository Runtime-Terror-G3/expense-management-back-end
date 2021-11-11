package service;

import service.exception.ServiceException;

public interface IService {
    Boolean deleteMonthlyBudget(int budgetId, int userId) throws ServiceException;
}
