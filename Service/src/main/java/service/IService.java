package service;

import domain.Expense;
import dto.ExpenseDto;
import service.exception.ServiceException;
import domain.User;

import java.util.Optional;

public interface IService {
    Optional<User> testAddUser(User user);

    Expense addExpense(ExpenseDto expenseDto) throws ServiceException;
}
