package controller;

import dto.MonthlyBudgetDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.IService;
import service.ServiceEmptyResponse;
import service.exception.ServiceException;

import java.util.Date;

@CrossOrigin
@RestController
@RequestMapping("api/expense-management")
public class MonthlyBudgetController {

    @Autowired
    private IService service;

    @DeleteMapping("/delete-monthly-budget/{budgetId}/{userId}")
    public ResponseEntity<?> deleteMonthlyBudget(
            @PathVariable int budgetId,
            @PathVariable int userId
    ){
        // TODO - get userId using authorization method; return unauthorized if is not valid; remove userId from path
        ServiceEmptyResponse response = service.deleteMonthlyBudget(budgetId,userId);
        switch (response.getStatus()){
            case 403:
                return new ResponseEntity<>(response.getErrorMessage(),HttpStatus.FORBIDDEN);
            case 500:
                return new ResponseEntity<>(response.getErrorMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("add-monthly-budget")
    public ResponseEntity<?> create(@RequestBody MonthlyBudgetDto monthlyBudgetDto) {
        try {
            return new ResponseEntity<>(service.addMonthlyBudget(monthlyBudgetDto), HttpStatus.OK);
        } catch (ServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update-monthly-budget/{budgetId}")
    public ResponseEntity<?> updateMonthlyBudget(@PathVariable int budgetId, @RequestBody MonthlyBudgetDto monthlyBudgetDto) {
        try {
            return new ResponseEntity<>(service.updateMonthlyBudget(budgetId, monthlyBudgetDto), HttpStatus.OK);
        } catch (ServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get-monthly-budgets")
    public ResponseEntity<?> getMonthlyBudgets(
            @RequestParam int userId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate
    ){
        //TODO get userId from token; remove userId param
        try {
            return new ResponseEntity<>(service.getMonthlyBudgets(userId, startDate, endDate), HttpStatus.OK);
        } catch (ServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
