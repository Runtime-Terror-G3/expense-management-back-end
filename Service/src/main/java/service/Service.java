package service;

import domain.Expense;
import domain.User;
import dto.ExpenseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import repository.IExpenseRepository;
import repository.IMonthlyBudgetRepository;
import repository.IUserRepository;
import service.exception.ServiceException;
import viewmodel.ExpenseViewModel;
import java.util.Optional;

@Component
public class Service implements IService{

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IExpenseRepository expenseRepository;
    @Autowired
    private IMonthlyBudgetRepository monthlyBudgetRepository;

    public Service(
            IUserRepository userRepository,
            IExpenseRepository expenseRepository,
            IMonthlyBudgetRepository monthlyBudgetRepository
    ) {
        this.userRepository = userRepository;
        this.expenseRepository = expenseRepository;
        this.monthlyBudgetRepository = monthlyBudgetRepository;
    }

    @Override
    public Optional<User> testAddUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public ExpenseViewModel addExpense(ExpenseDto expenseDto) throws ServiceException {
        Expense expense = Expense.fromExpenseDto(expenseDto);

        Optional<Expense> savedExpense = expenseRepository.save(expense);

        if (savedExpense.isPresent()) {
            throw new ServiceException("An error occurred while saving the expense.");
        }

        return ExpenseViewModel.fromExpense(expense);
    }
}
