package controller;

import domain.Expense;
import dto.ExpenseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.IService;
import service.exception.ServiceException;

@CrossOrigin
@RestController
@RequestMapping("api/expense-management")
public class ExpenseController {
    @Autowired
    private IService service;

    //TODO: is this the final endpoint decision
    //      or should we go with a restful design like "/expenses"?

    //      also i think we should rethink our expense/user entities structure
    //      or remap hibernate entities bcs a user has expenses, an expense
    //      has a user and hibernate will look recursively until stack overflows
    //      when we select a user or an expense
    @PostMapping("/add-expense")
    public ResponseEntity<?> create(@RequestBody ExpenseDto expenseDto) {
        Expense expense;

        try {
            expense = service.addExpense(expenseDto);
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(expense, HttpStatus.OK);
    }
}
