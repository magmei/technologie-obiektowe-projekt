package pl.agh.edu.to.aleksandria.model.role;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    public Role() {

    }

    public Role(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
