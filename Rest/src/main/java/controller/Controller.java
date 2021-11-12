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

@CrossOrigin
@RestController
@RequestMapping("api/expense-management")
public class Controller {
    @Autowired
    private IService service;

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

                return new ResponseEntity<String>(token, HttpStatus.OK);
            } else {
                return new ResponseEntity<String>("Invalid credentials", HttpStatus.NOT_FOUND);
            }
        } catch(JSONException exception) {
            return new ResponseEntity<String>("Invalid request", HttpStatus.BAD_REQUEST);
        }
    }

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
