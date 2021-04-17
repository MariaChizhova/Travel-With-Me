package com.example.demo.models;

import com.example.demo.requests.UserUpdateRequest;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName = null;
    private String lastName = null;
    private String email = null;
    private String avatar = null;

    public User() {
    }

    public User(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatar() {
        return avatar;
    }

    public User setAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    public User setAll(UserUpdateRequest user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        return this;
    }
}
