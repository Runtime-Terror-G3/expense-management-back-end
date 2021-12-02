package controller;

import domain.User;
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
import java.util.Optional;

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

        if (bearerToken != null && bearerToken.startsWith("Bearer")) {
            String token = bearerToken.substring(7);
            Optional<User> user = service.getTokenUser(token);
            if (user.isPresent()) {
                int userId = user.get().getId();
                ServiceEmptyResponse response = service.deleteMonthlyBudget(budgetId, userId);
                switch (response.getStatus()) {
                    case 403:
                        return new ResponseEntity<>(response.getErrorMessage(), HttpStatus.FORBIDDEN);
                    case 500:
                        return new ResponseEntity<>(response.getErrorMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                }
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>("Unauthorized",HttpStatus.UNAUTHORIZED);
            }
        }
        else{
            return new ResponseEntity<>("Forbidden",HttpStatus.FORBIDDEN);
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

        if (bearerToken != null && bearerToken.startsWith("Bearer")) {
            String token = bearerToken.substring(7);
            Optional<User> user = service.getTokenUser(token);
            if (user.isPresent()) {
                int userId = user.get().getId();
                try {
                    return new ResponseEntity<>(service.getMonthlyBudgets(userId, startDate, endDate), HttpStatus.OK);
                } catch (ServiceException e) {
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
                }
            }
            else{
                return new ResponseEntity<>("Unauthorized",HttpStatus.UNAUTHORIZED);
            }
        }
        else{
            return new ResponseEntity<>("Forbidden",HttpStatus.FORBIDDEN);
        }
    }
}
