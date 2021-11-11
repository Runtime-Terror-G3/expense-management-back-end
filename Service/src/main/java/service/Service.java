package service;

import domain.MonthlyBudget;
import domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import repository.IExpenseRepository;
import repository.IMonthlyBudgetRepository;
import repository.IUserRepository;
import service.exception.ServiceException;

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
    public Boolean deleteMonthlyBudget(int budgetId, int userId) throws ServiceException {

        Optional<MonthlyBudget> budgetToDelete = monthlyBudgetRepository.findOne(budgetId);

        if (budgetToDelete.isPresent()) {
            if (budgetToDelete.get().getUser().getId() != userId)
                throw new ServiceException("Not allowed to delete this resource");

            Optional<MonthlyBudget> deletedBudget = monthlyBudgetRepository.delete(budgetId);

            if (!deletedBudget.isPresent())
                return false;

        }else{
            throw new ServiceException("Monthly budget not found");
        }
        return true;
    }
}
