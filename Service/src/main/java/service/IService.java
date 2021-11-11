package service;

import domain.Expense;
import domain.User;
import dto.ExpenseDto;
import service.exception.ServiceException;

import java.util.Optional;

public interface IService {
    Optional<User> testAddUser(User user);

    Expense addExpense(ExpenseDto expenseDto) throws ServiceException;
}
