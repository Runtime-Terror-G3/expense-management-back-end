package controller;

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
            case 404:
                return new ResponseEntity<>(response.getErrorMessage(),HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
