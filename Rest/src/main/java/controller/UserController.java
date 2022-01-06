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

import java.util.Optional;

/**
 * The REST Controller responsible for exposing the endpoints related to the login functionality:
 * - sign-in
 * - auth-check
 */
@CrossOrigin
@RestController
@RequestMapping("api/expense-management")
public class UserController {
    @Autowired
    private IService service;

    /**
     * Sign in into the app
     * Method: POST
     * @param body the request body - a String containing
     *             email: String
     *             password: String (hex string of a sha256 hash)
     * @return the logged in User if the login was successful or an error message otherwise
     */
    @PostMapping("/sign-in")
    public ResponseEntity<?> login(@RequestBody String body) {
        try {
            JSONObject params = new JSONObject(body);
            String email = (String) params.get("email"),
                    password;

            try {
                // The password should be a hex string
                password = new String(Utils.hexStringToByteArray((String) params.get("password")));
            } catch(IllegalArgumentException e) {
                return new ResponseEntity<String>("Invalid request", HttpStatus.BAD_REQUEST);
            }

            Optional<User> optional_user = service.login(email, password);

            if(optional_user.isPresent()) {
                User user = optional_user.get();
                String token = service.generateUserToken(user);
                String responseJson = "{\"token\": \"" + token + "\"}";

                return new ResponseEntity<String>(responseJson, HttpStatus.OK);
            } else {
                return new ResponseEntity<String>("Invalid credentials", HttpStatus.NOT_FOUND);
            }
        } catch(JSONException exception) {
            return new ResponseEntity<String>("Invalid request", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Check the authentication token
     * @param token the token to be checked
     * @return the user's first name, if the token is valid, or an error message otherwise
     */
    @PostMapping("/auth-check")
    public ResponseEntity<?> authenticationCheck(@RequestBody String token) {
        Optional<User> user = service.getTokenUser(token);

        if(user.isPresent()) {
            return new ResponseEntity<String>(user.get().getFirstName(), HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("Invalid token", HttpStatus.FORBIDDEN);
        }
    }
}
