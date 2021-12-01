package controller;

import domain.ExpenseCategory;
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
    public ResponseEntity<?> delete(@PathVariable int expenseId, @PathVariable int userId) {
        //TODO get userId from token; remove userId param
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

    @GetMapping("/get-expenses")
    public ResponseEntity<?> getExpenses(
            @RequestParam int userId,
            @RequestParam String category,
            @RequestParam long startDate,
            @RequestParam long endDate
    ) {
        //TODO get userId from token; remove userId param
        try {
            return new ResponseEntity<>(service.getExpenses(userId, category, startDate, endDate), HttpStatus.OK);
        } catch (ServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
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
            @RequestParam int userId,
            @RequestParam String granularity,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam String category) {
        try {
            return new ResponseEntity<>(service.getTotalExpensesInTime(userId, granularity, startDate, endDate, category), HttpStatus.OK);
        } catch (ServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/category-total")
    public ResponseEntity<?> getExpensesTotalByCategory(@RequestParam("userId") int userId,
                                                        @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                                        @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        Map<ExpenseCategory, Double> result = service.getExpenseTotalByCategory(userId, start, end);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
