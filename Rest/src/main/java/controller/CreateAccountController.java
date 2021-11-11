package controller;

import domain.User;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.IService;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("api/expense-management")
public class CreateAccountController {
    @Autowired
    private IService service;

    @PostMapping("/create-account")
    public ResponseEntity<String> createAccount(@RequestBody String body) {
        try {
            JSONObject params = new JSONObject(body);
            String email = String.valueOf(params.get("email"));
            String password = Arrays.toString(Base64.getDecoder().decode((String) params.get("password")));
            String firstName = String.valueOf(params.get("firstName"));
            String lastName = String.valueOf(params.get("lastName"));
            Date dateOfBirth = new Date(new Timestamp((Long) params.get("dateOfBirth")).getTime());

            Optional<User> optionalUser = service.createAccount(email, firstName, lastName, dateOfBirth, password);

            if(optionalUser.isEmpty()) {
                return new ResponseEntity<>("Account created successfully", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("This email already has an associated account", HttpStatus.CONFLICT);
            }
        } catch(JSONException e) {
            return new ResponseEntity<>("Invalid request", HttpStatus.BAD_REQUEST);
        }
    }
}
