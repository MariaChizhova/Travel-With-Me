package com.example.travelwithme.pojo;

public class User {
    private final Long userID;
    private final String firstName;
    private final String lastName;
    private final String description;
    private final String location;
    private final String email;
    private final String avatar;
    private final int followersNumber;
    private int followingsNumber;

    public User(Long userID, String avatar, String firstName, String lastName, String email,
                String description, String location, int followingsNumber, int followersNumber) {
        this.userID = userID;
        this.avatar = avatar;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.description = description;
        this.location = location;
        this.followingsNumber = followingsNumber;
        this.followersNumber = followersNumber;
    }

    public Long getUserID() {
        return userID;
    }

    public String getAvatar() {
        return avatar;
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

    public void decFollowingsNumber() {
        followingsNumber--;
    }

    public void incFollowingsNumber() {
        followingsNumber++;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public int getFollowingsNumber() {
        return followingsNumber;
    }

    public int getFollowersNumber() {
        return followersNumber;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (userID != user.userID) return false;
        if (followingsNumber != user.followingsNumber) return false;
        if (followersNumber != user.followersNumber) return false;
        if (avatar != null ? !avatar.equals(user.avatar) : user.avatar != null)
            return false;
        if (!firstName.equals(user.firstName)) return false;
        if (!lastName.equals(user.lastName)) return false;
        if (description != null ? !description.equals(user.description) : user.description != null) {
            return false;
        }
        return location != null ? location.equals(user.location) : user.location == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (userID ^ (userID >>> 32));
        result = 31 * result + (avatar != null ? avatar.hashCode() : 0);
        result = 31 * result + firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + followingsNumber;
        result = 31 * result + followersNumber;
        return result;
    }
}