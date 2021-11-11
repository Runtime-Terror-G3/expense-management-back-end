package client;

import domain.User;

import java.sql.Date;

public class Main {
    public static void main(String[] args) throws Exception {
        Client client = new Client();

//        System.out.println("Search artist with ID 1");
//        User user = client.findOne(1L);
//        System.out.println(user);

//        System.out.println("Display all artists");
//        for(User theUser : client.findAll())
//            System.out.println(theUser);

        System.out.println("Add user");
        User user1 = client.save(new User("myEmail@rt.com", "Mohi", "RZV", Date.valueOf("2000-09-21"), "abcdef"));
        System.out.println("Saved user "+user1);

        System.out.println("Display all users");
        for(User theUser : client.findAll())
            System.out.println(theUser);
    }
}