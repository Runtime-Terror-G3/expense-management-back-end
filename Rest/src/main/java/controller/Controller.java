package controller;

import domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.IService;

import java.util.Date;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("api/expense-management")
public class Controller {
    @Autowired
    private IService service;

    @PostMapping("/test")
    public ResponseEntity<?> testAddUser(){
        User userToAdd = new User("ana.pop@gmail.com","Ana","Pop", new Date(),"1234");

        Optional<User> response = service.testAddUser(userToAdd);

        if (response.isPresent()){
            return new ResponseEntity<String>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<User>(userToAdd, HttpStatus.OK);
    }
}
