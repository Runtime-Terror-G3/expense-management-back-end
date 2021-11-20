package controller;

import domain.User;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.IService;
import utils.Utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
            // The password should be a hex string
            String password = new String(Utils.hexStringToByteArray((String) params.get("password")));
            String firstName = String.valueOf(params.get("firstName"));
            String lastName = String.valueOf(params.get("lastName"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date dateOfBirth = formatter.parse((String) params.get("dateOfBirth"));

            Optional<User> optionalUser = service.createAccount(email, firstName, lastName, dateOfBirth, password);

            if(optionalUser.isEmpty()) {
                return new ResponseEntity<>("Account created successfully", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Could not create account!", HttpStatus.CONFLICT);
            }
        } catch(JSONException | IllegalArgumentException | ParseException e) {
            return new ResponseEntity<>("Invalid request", HttpStatus.BAD_REQUEST);
        }
    }
}
