package pl.agh.edu.to.aleksandria.model.user.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateUserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String address;
    private String roleName;

    public CreateUserRequest(String firstName, String lastName, String email, String password, String address, String roleName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.address = address;
        this.roleName = roleName;
    }

}
