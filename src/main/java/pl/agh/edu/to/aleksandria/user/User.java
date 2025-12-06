package pl.agh.edu.to.aleksandria.user;

import jakarta.persistence.*;
import pl.agh.edu.to.aleksandria.role.Role;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String address;
    private String email;

    @ManyToOne
    private Role role;

    public User() {}

    public User(String firstName, String lastName,  String address, String email, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.role = role;
    }

    public Long getId() {
        return id;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + ", " + role;
    }

}
