package controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import domain.User;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.IService;
import utils.Utils;

import java.util.Date;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("api/expense-management")
public class Controller {
    @Autowired
    private IService service;
    private static long tokenTime = 8 * 60 * 60 * 1000; // 8 hours

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
                Algorithm algorithm = Algorithm.HMAC256("super secret token secret");
                long currentTime = System.currentTimeMillis();

                String token = JWT.create()
                        .withClaim("ID", user.getId())
                        .withClaim("first_name", user.getFirstName())
                        .withClaim("last_name", user.getFirstName())
                        .withClaim("iat", currentTime)
                        .withClaim("exp", currentTime + tokenTime)
                        .sign(algorithm);

                return new ResponseEntity<String>(token, HttpStatus.OK);
            } else {
                return new ResponseEntity<String>("Invalid credentials", HttpStatus.NOT_FOUND);
            }
        } catch(JSONException exception) {
            return new ResponseEntity<String>("Invalid request", HttpStatus.BAD_REQUEST);
        }
    }
}
