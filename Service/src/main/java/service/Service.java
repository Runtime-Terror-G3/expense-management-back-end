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
    public ServiceEmptyResponse deleteMonthlyBudget(int budgetId, int userId) {
        ServiceEmptyResponse response = new ServiceEmptyResponse(200,"");

        Optional<MonthlyBudget> budgetToDelete = monthlyBudgetRepository.findOne(budgetId);

        if (budgetToDelete.isPresent()) {
            if (budgetToDelete.get().getUser().getId() != userId) {
                response.setStatus(403);
                response.setErrorMessage("Not allowed to delete this resource");
            }

            Optional<MonthlyBudget> deletedBudget = monthlyBudgetRepository.delete(budgetId);

            if (!deletedBudget.isPresent()) {
                response.setStatus(500);
                response.setErrorMessage("Internal Server Error");
            }
        }
        return response;
    }
}
