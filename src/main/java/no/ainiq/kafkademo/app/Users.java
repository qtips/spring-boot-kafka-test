package no.ainiq.kafkademo.app;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Users {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private int age;

    public static Users newInstance(String message, int age) {
        Users user = new Users();
        user.name = message;
        user.age = age;
        return user;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}