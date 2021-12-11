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

import static utils.Utils.validateToken;

@CrossOrigin
@RestController
@RequestMapping("api/expense-management")
public class MonthlyBudgetController {

    @Autowired
    private IService service;

    @DeleteMapping("/delete-monthly-budget/{budgetId}")
    public ResponseEntity<?> deleteMonthlyBudget(
            @PathVariable int budgetId,
            @RequestHeader("Authorization") String bearerToken
    ){

        try {
            int userId = validateToken(bearerToken, service);
            ServiceEmptyResponse response = service.deleteMonthlyBudget(budgetId, userId);
            if (response.getStatus() == 403) {
                return new ResponseEntity<>(response.getErrorMessage(), HttpStatus.FORBIDDEN);
            } else if (response.getStatus() == 500) {
                return new ResponseEntity<>(response.getErrorMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception e ){
            String message = e.getMessage();
            if ("Unauthorized".equals(message)) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
            } else if ("Forbidden".equals(message)) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
            }
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
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
            @RequestHeader("Authorization") String bearerToken,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate
    ){

        try {
            int userId = validateToken(bearerToken, service);
            return new ResponseEntity<>(service.getMonthlyBudgets(userId, startDate, endDate), HttpStatus.OK);
        }
        catch (Exception e ){
            String message = e.getMessage();
            if ("Unauthorized".equals(message)) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
            } else if ("Forbidden".equals(message)) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
            }
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
