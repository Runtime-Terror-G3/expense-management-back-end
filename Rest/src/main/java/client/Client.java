package client;

import domain.User;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Callable;

public class Client {
    String URL = "http://localhost:8080/create-account";
    RestTemplate template = new RestTemplate();

    private <T> T execute(Callable<T> callable) throws Exception {
        return callable.call();
    }

    public User save(User user) throws Exception {
        return execute(() -> template.postForObject(URL, user, User.class));
    }

    public User findOne(Long id) throws Exception {
        return execute(() -> template.getForObject(URL + '/' + id.toString(), User.class));
    }

    public User[] findAll() throws Exception {
        return execute(() -> template.getForObject(URL, User[].class));
    }
}
