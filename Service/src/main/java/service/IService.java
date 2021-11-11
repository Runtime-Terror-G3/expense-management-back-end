package service;

import domain.Expense;
import domain.User;
import dto.ExpenseDto;
import service.exception.ServiceException;
import viewmodel.ExpenseViewModel;

import java.util.Optional;

public interface IService {
    Optional<User> testAddUser(User user);

    ExpenseViewModel addExpense(ExpenseDto expenseDto) throws ServiceException;
}
