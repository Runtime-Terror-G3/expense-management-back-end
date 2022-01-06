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

import static utils.Utils.validateToken;

/**
 * The REST Controller responsible for exposing the endpoints related to managing expenses:
 * - add-expense
 * - delete-expense
 * - get-expenses
 * - update-expense
 * - total-expenses-in-time
 * - category-total
 */
@CrossOrigin
@RestController
@RequestMapping("api/expense-management")
public class ExpenseController {
    @Autowired
    private IService service;

    /**
     * Endpoint for adding an expense
     * Method: POST
     * Requires Authorization header
     * @param expenseDto an ExpenseDto ; represents the request body
     * @param bearerToken a String containing the authorization token ; represents the Authorization header
     * @return a ResponseEntity with the ExpenseViewModel corresponding to the added expense
     *         or with an error message if the expense could not be added
     */
    @PostMapping("/add-expense")
    public ResponseEntity<?> create(@RequestBody ExpenseDto expenseDto, @RequestHeader("Authorization") String bearerToken) {
        try {
            int userId = validateToken(bearerToken, service);
            expenseDto.setUserId(userId);

            return new ResponseEntity<>(service.addExpense(expenseDto), HttpStatus.OK);
        } catch (ServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * Endpoint for deleting an expense
     * Method: DELETE
     * Requires Authorization header
     * Query parameters:
     *      category: String (one of the expense categories or All)
     *      startDate: String (UNIX timestamp)
     *      endDate: String (UNIX timestamp)
     * @param expenseId the id of the expense to delete
     * @param bearerToken a String containing the authorization token ; represents the Authorization header
     * @return a ResponseEntity with the ExpenseViewModel corresponding to the added expense
     *         or with an error message if the expense could not be added
     */
    @DeleteMapping("/delete-expense/{expenseId}")
    public ResponseEntity<?> delete(@PathVariable int expenseId, @RequestHeader("Authorization") String bearerToken) {
        try {
            int userId = validateToken(bearerToken, service);
            return new ResponseEntity<>(service.deleteExpense(expenseId, userId), HttpStatus.OK);
        }
        catch (Exception e ){
            String message = e.getMessage();
            if ("Forbidden access to this expense".equals(message)) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
            } else if ("Internal server error".equals(message)) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint for retrieving a collection of expenses filtered by a category and from a specified time interval
     * Method: GET
     * Requires Authorization header
     * @param bearerToken a String containing the authorization token ; represents the Authorization header
     * @param category the category of the desired expenses
     * @param startDate the start of the time interval
     * @param endDate the end of the time interval
     * @return a ResponseEntity with the requested expenses, on success, or with an error message, otherwise
     */
    @GetMapping("/get-expenses")
    public ResponseEntity<?> getExpenses(
            @RequestHeader("Authorization") String bearerToken,
            @RequestParam String category,
            @RequestParam long startDate,
            @RequestParam long endDate
    ) {
        try {
            int userId = validateToken(bearerToken, service);
            return new ResponseEntity<>(service.getExpenses(userId, category, startDate, endDate), HttpStatus.OK);
        }
        catch (Exception e ){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint for updating an expense
     * Method: POST
     * Requires Authorization header
     * @param expenseDto the new data for the expense
     * @param expenseId the id of the expense to be updated
     * @param bearerToken a String containing the authorization token ; represents the Authorization header
     * @return the viewModel of the updated expense
     */
    @PostMapping("/update-expense/{expenseId}")
    public ResponseEntity<?> update(@RequestBody ExpenseDto expenseDto, @PathVariable int expenseId,
                                    @RequestHeader("Authorization") String bearerToken ) {
        try {
            int userId = validateToken(bearerToken, service);
            expenseDto.setUserId(userId);

            return new ResponseEntity<>(service.updateExpense(expenseDto, expenseId), HttpStatus.OK);
        } catch (ServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint for retrieving an overview of expenses from a specified time interval
     * Method: GET
     * Requires Authorization header
     * @param bearerToken a String containing the authorization token ; represents the Authorization header
     * @param granularity the granularity of the statistic
     * @param startDate the start of the time interval
     * @param endDate the end of the time interval
     * @param category the category for which the report is done
     * @return a collection of TotalExpensesDto representing the requested report
     */
    @GetMapping("/total-expenses-in-time")
    public ResponseEntity<?> getTotalExpensesInTime(
            @RequestHeader("Authorization") String bearerToken,
            @RequestParam String granularity,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam String category) {

        try {
            int userId = validateToken(bearerToken, service);
            return new ResponseEntity<>(service.getTotalExpensesInTime(userId, granularity, startDate, endDate, category), HttpStatus.OK);
        }
        catch (Exception e ){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint for retrieving the total amount of a user's expenses within a time period, grouped by expense category
     * Method: GET
     * Requires Authorization header
     * @param bearerToken a String containing the authorization token ; represents the Authorization header
     * @param start the start of the time interval
     * @param end the end of the time interval
     * @return a {@code Map} where each key represents an {@code ExpenseCategory} and the value, the total amount of expenses of that category
     */
    @GetMapping(value = "/category-total")
    public ResponseEntity<?> getExpensesTotalByCategory(@RequestHeader("Authorization") String bearerToken,
                                                        @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                                        @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        try {
            int userId = validateToken(bearerToken, service);
            Map<ExpenseCategory, Double> result = service.getExpenseTotalByCategory(userId, start, end);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        catch (Exception e ){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
