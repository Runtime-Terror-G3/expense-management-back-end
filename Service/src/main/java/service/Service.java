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
        Expense expense = expenseDtoToExpense(expenseDto);

        Optional<Expense> savedExpense = expenseRepository.save(expense);

        if (savedExpense.isPresent()) {
            throw new ServiceException("An error occurred while saving the expense.");
        }

        return expenseToExpenseViewModel(expense);
    }

    /**
     * transforms an ExpenseDto object to an Expense object
     *
     * @throws ServiceException if the userId of the expenseDto does not match any user
     */
    private Expense expenseDtoToExpense(ExpenseDto expenseDto) throws ServiceException {
        Optional<User> optionalUser = userRepository.findOne(expenseDto.getUserId());

        if (optionalUser.isEmpty()) {
            throw new ServiceException("The user does not exist.");
        }

        return new Expense(
                expenseDto.getAmount(),
                expenseDto.getCategory(),
                expenseDto.getDate(),
                optionalUser.get()
        );
    }

    /**
     * transforms an Expense object to and ExpenseViewModel object
     */
    private ExpenseViewModel expenseToExpenseViewModel(Expense expense) {
        return new ExpenseViewModel(
                expense.getId(),
                expense.getAmount(),
                expense.getCategory(),
                expense.getDate()
        );
    }
}
