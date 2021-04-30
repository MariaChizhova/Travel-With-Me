package server.requests;

public class UserEditRequest {

    private final Long id;
    private String firstName = null;
    private String lastName = null;

    public UserEditRequest(Long id) {
        this.id = id;
    }

    public UserEditRequest(Long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
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

