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

    @PostMapping("/add-expense")
    public ResponseEntity<?> create(@RequestBody ExpenseDto expenseDto) {
        try {
            return new ResponseEntity<>(service.addExpense(expenseDto), HttpStatus.OK);
        } catch (ServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get-expenses")
    public ResponseEntity<?> getExpenses(
        @RequestParam int userId,
        @RequestParam  String category,
        @RequestParam long startDate,
        @RequestParam long endDate
    ){
        //TODO get userId from token; remove userId param
        try {
            return new ResponseEntity<>(service.getExpenses(userId, category, startDate, endDate), HttpStatus.OK);
        } catch (ServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
