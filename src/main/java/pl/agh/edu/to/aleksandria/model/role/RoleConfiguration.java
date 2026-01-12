package pl.agh.edu.to.aleksandria.model.role;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@AllArgsConstructor
public class RoleConfiguration {

    RoleRepository roleRepository;

//    @PostConstruct
//    private void initRoles() {
//        Role readerRole = new Role("reader");
//        Role librarianRole = new Role("librarian");
//        Role adminRole = new Role("admin");
//
//        roleRepository.saveAll(List.of(readerRole, librarianRole, adminRole));
//    }
}
