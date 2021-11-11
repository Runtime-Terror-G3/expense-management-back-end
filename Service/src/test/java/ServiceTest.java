import org.junit.jupiter.api.Test;
import repository.IUserRepository;
import repository.hibernate.UserHbRepository;
import service.Service;

import java.sql.Date;

class ServiceTest {

    @Test
    void testSaveUser(){
        IUserRepository userRepository = new UserHbRepository();
        Service service = new Service(userRepository);
        service.signUp("rsalsigan@gmail.com", "Mohi", "RZV", Date.valueOf("2000-09-21"), "abcdef");


        assert(userRepository.findOne(1).get().getEmail().equals("rsalsigan@gmail.com"));
    }
}
