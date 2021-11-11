package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.IService;
import service.exception.ServiceException;

@CrossOrigin
@RestController
@RequestMapping("api/expense-management/monthly-budgets")
public class MonthlyBudgetController {

    @Autowired
    private IService service;

    @DeleteMapping("/delete-monthly-budget/{budgetId}/{userId}")
    public ResponseEntity<?> deleteMonthlyBudget(
            @PathVariable int budgetId,
            @PathVariable int userId
    ){
        // TODO - get userId using authorization method; return unauthorized if is not valid; remove userId from path
        Boolean isDeleteSuccess = false;
        try {
            isDeleteSuccess = service.deleteMonthlyBudget(budgetId,userId);
        } catch (ServiceException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        if (! isDeleteSuccess)
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
