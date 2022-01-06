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

/**
 * The REST Controller responsible for exposing the endpoints related to managing monthly budgets:
 * - delete-monthly-budget
 * - add-monthly-budget
 * - update-monthly-budget
 * - get-monthly-budgets
 */
@CrossOrigin
@RestController
@RequestMapping("api/expense-management")
public class MonthlyBudgetController {

    @Autowired
    private IService service;

    /**
     * Endpoint for deleting a monthly budget
     * Method: DELETE
     * Requires Authorization header
     * @param budgetId the id of the monthly budget to delete
     * @param bearerToken a String containing the authorization token ; represents the Authorization header
     * @return a ServiceEmptyResponse with status=200 in case of success
     * a ServiceEmptyResponse with status=403 in case the budget with
     * the given id is not owned by the user with the given userId
     * a ServiceEmptyResponse with status=500 in case the delete operation fails
     */
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
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Add a monthly budget
     * Method: POST
     * Requires Authorization header
     * @param monthlyBudgetDto a MonthlyBudgetDto corresponding to the monthly budget to be added
     * @param bearerToken a String containing the authorization token ; represents the Authorization header
     * @return the view model of the added monthly budget, if successful
     */
    @PostMapping("add-monthly-budget")
    public ResponseEntity<?> create(@RequestBody MonthlyBudgetDto monthlyBudgetDto,
                                    @RequestHeader("Authorization") String bearerToken) {
        try {
            int userId = validateToken(bearerToken, service);
            monthlyBudgetDto.setUserId(userId);

            return new ResponseEntity<>(service.addMonthlyBudget(monthlyBudgetDto), HttpStatus.OK);
        } catch (ServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Update a monthly budget
     * Method: POST
     * Requires Authorization header
     * @param budgetId the id of the monthly budget to be updated
     * @param monthlyBudgetDto the new data for the monthly budget
     * @param bearerToken a String containing the authorization token ; represents the Authorization header
     * @return the viewModel of the updated monthly budget
     */
    @PutMapping("/update-monthly-budget/{budgetId}")
    public ResponseEntity<?> updateMonthlyBudget(@PathVariable int budgetId, @RequestBody MonthlyBudgetDto monthlyBudgetDto,
                                                 @RequestHeader("Authorization") String bearerToken) {
        try {
            int userId = validateToken(bearerToken, service);
            monthlyBudgetDto.setUserId(userId);

            return new ResponseEntity<>(service.updateMonthlyBudget(budgetId, monthlyBudgetDto), HttpStatus.OK);
        } catch (ServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get the monthly budgets for a user during a period of time
     * Method: GET
     * Requires Authorization header
     * @param bearerToken a String containing the authorization token ; represents the Authorization header4
     * @param startDate the start of the time interval
     * @param endDate the end of the time interval
     * @return a collection of view models for the monthly budgets available in the given time interval
     */
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
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
