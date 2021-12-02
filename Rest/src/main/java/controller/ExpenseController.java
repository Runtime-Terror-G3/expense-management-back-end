package controller;

import domain.ExpenseCategory;
import domain.User;
import dto.ExpenseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.IService;
import service.exception.ServiceException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

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


    @DeleteMapping("/delete-expense/{expenseId}")
    public ResponseEntity<?> delete(@PathVariable int expenseId, @RequestHeader("Authorization") String bearerToken) {

        if (bearerToken != null && bearerToken.startsWith("Bearer")) {
            String token = bearerToken.substring(7);
            Optional<User> user = service.getTokenUser(token);
            if (user.isPresent()) {
                int userId = user.get().getId();
                try {
                    return new ResponseEntity<>(service.deleteExpense(expenseId, userId), HttpStatus.OK);
                } catch (ServiceException e) {
                    if (e.getMessage().equals("Forbidden access to this expense"))
                        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
                    else if (e.getMessage().equals("Internal server error"))
                        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                    else
                        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
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

    @GetMapping("/get-expenses")
    public ResponseEntity<?> getExpenses(
            @RequestHeader("Authorization") String bearerToken,
            @RequestParam String category,
            @RequestParam long startDate,
            @RequestParam long endDate
    ) {

        if (bearerToken != null && bearerToken.startsWith("Bearer")) {
            String token = bearerToken.substring(7);
            Optional<User> user = service.getTokenUser(token);
            if (user.isPresent()) {
                int userId = user.get().getId();
                try {
                    return new ResponseEntity<>(service.getExpenses(userId, category, startDate, endDate), HttpStatus.OK);
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

    @PostMapping("/update-expense/{expenseId}")
    public ResponseEntity<?> update(@RequestBody ExpenseDto expenseDto, @PathVariable int expenseId) {
        try {
            return new ResponseEntity<>(service.updateExpense(expenseDto, expenseId), HttpStatus.OK);
        } catch (ServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/total-expenses-in-time")
    public ResponseEntity<?> getTotalExpensesInTime(
            @RequestHeader("Authorization") String bearerToken,
            @RequestParam String granularity,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam String category) {

        if (bearerToken != null && bearerToken.startsWith("Bearer")) {
            String token = bearerToken.substring(7);
            Optional<User> user = service.getTokenUser(token);
            if (user.isPresent()) {
                int userId = user.get().getId();
                try {
                    return new ResponseEntity<>(service.getTotalExpensesInTime(userId, granularity, startDate, endDate, category), HttpStatus.OK);
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

    @GetMapping(value = "/category-total")
    public ResponseEntity<?> getExpensesTotalByCategory(@RequestHeader("Authorization") String bearerToken,
                                                        @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                                        @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        if (bearerToken != null && bearerToken.startsWith("Bearer")) {
            String token = bearerToken.substring(7);
            Optional<User> user = service.getTokenUser(token);
            if (user.isPresent()) {
                int userId = user.get().getId();
                Map<ExpenseCategory, Double> result = service.getExpenseTotalByCategory(userId, start, end);
                return new ResponseEntity<>(result, HttpStatus.OK);
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
