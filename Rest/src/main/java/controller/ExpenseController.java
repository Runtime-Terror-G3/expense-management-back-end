package controller;

import domain.Expense;
import dto.ExpenseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.IService;
import service.ServiceEmptyResponse;
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


    @DeleteMapping("/delete-expense/{expenseId}/{userId}")
    public ResponseEntity<?> delete(@PathVariable int expenseId,@PathVariable int userId){
        ServiceEmptyResponse response=service.deleteExpense(expenseId,userId);
        switch(response.getStatus()){
            case 403:
                return new ResponseEntity<>(response.getErrorMessage(),HttpStatus.FORBIDDEN);
            case 500:
                return new ResponseEntity<>(response.getErrorMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
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
