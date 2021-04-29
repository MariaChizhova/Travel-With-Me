package server.models;

import server.requests.UserUpdateRequest;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;    // unchanged

    private String firstName = null;
    private String lastName = null;
    private String email = null;    // unchanged
    private String avatar = null;    // name file in the amazon s3 database
    private int numberFollowers = 0;
    private int numberFollowing = 0;

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

    public User incNumberFollowers() {
        this.numberFollowers++;
        return this;
    }

    public User decNumberFollowers() {
        this.numberFollowers--;
        return this;
    }

    public User incNumberFollowing() {
        this.numberFollowing++;
        return this;
    }

    public User decNumberFollowing() {
        this.numberFollowing--;
        return this;
    }

}
