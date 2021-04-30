package server.requests;

public class UserEditRequest {

    private final Long userId;
    private String firstName = null;
    private String lastName = null;

    public UserEditRequest(Long userId) {
        this.userId = userId;
    }

    public UserEditRequest(Long userId, String firstName, String lastName) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}

