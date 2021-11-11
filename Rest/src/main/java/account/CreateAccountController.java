package account;

import domain.User;
import org.springframework.web.bind.annotation.*;
import repository.IUserRepository;
import repository.hibernate.UserHbRepository;

@RestController
@RequestMapping("/create-account")
public class CreateAccountController {
    private final IUserRepository repository;
    public CreateAccountController(){
        repository = new UserHbRepository();
    }

    @PostMapping
    public User save(@RequestBody User user){
        System.out.println(user);
        repository.save(user);
        return user;
    }
}
