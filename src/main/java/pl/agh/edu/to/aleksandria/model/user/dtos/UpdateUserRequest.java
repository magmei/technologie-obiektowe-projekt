package pl.agh.edu.to.aleksandria.model.user.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UpdateUserRequest {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
}
