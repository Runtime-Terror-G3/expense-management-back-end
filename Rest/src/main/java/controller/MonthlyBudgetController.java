package controller;

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
}
