package controller;

import dto.ExpenseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.IService;
import service.exception.ServiceException;
import viewmodel.ExpenseViewModel;

@CrossOrigin
@RestController
@RequestMapping("api/expense-management")
public class ExpenseController {
    @Autowired
    private IService service;

    @PostMapping("/add-expense")
    public ResponseEntity<?> create(@RequestBody ExpenseDto expenseDto) {
        ExpenseViewModel expense;

        try {
            expense = service.addExpense(expenseDto);
        } catch (ServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(expense, HttpStatus.OK);
    }
}
