package pl.agh.edu.to.aleksandria.model.user.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateUserRequest {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String address;

    public UpdateUserRequest(String firstName, String lastName, String email, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
    }

}
