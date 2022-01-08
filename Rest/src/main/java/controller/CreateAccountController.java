package controller;

import domain.UserRequest;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.IService;
import utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

/**
 * The REST Controller responsible for exposing the endpoints related to creating an account:
 * - create-account
 * - activate-account
 */
@CrossOrigin
@RestController
@RequestMapping("api/expense-management")
public class CreateAccountController {
    @Autowired
    private IService service;

    /**
     * Endpoint for creating an account
     * Method: POST
     * @param body a JSON string containing ; represents the request body
     *      email: String
     *      password: String (hex String of a sha256 hash)
     *      firstName: String
     *      lastName: String
     *      dateOfBirth: String (yyyy-mm-dd)
     * @return a ResponseEntity with a message corresponding to the result of the request
     */
    @PostMapping("/create-account")
    public ResponseEntity<String> createAccount(@RequestBody String body) {
        try {
            JSONObject params = new JSONObject(body);
            String email = String.valueOf(params.get("email"));
            // The password should be a hex string
            String password = new String(Utils.hexStringToByteArray((String) params.get("passwordHash")));
            String firstName = String.valueOf(params.get("firstName"));
            String lastName = String.valueOf(params.get("lastName"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date dateOfBirth = formatter.parse((String) params.get("dateOfBirth"));

            Optional<UserRequest> optionalUser = service.createAccount(email, firstName, lastName, dateOfBirth, password);

            if(optionalUser.isEmpty()) {
                return new ResponseEntity<>("Account created successfully", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Could not create account!", HttpStatus.CONFLICT);
            }
        } catch(JSONException | IllegalArgumentException | ParseException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Invalid request", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint for activating an account
     * Method: POST
     * @param activationToken a String containing the activation token ; represents the request body
     * @return a ResponseEntity with a message corresponding to the result of the request
     */
    @PostMapping("/activate-account")
    public ResponseEntity<?> activateAccount(@RequestBody String activationToken){
        if (service.activateAccount(activationToken))
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>("Could not activate account", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
