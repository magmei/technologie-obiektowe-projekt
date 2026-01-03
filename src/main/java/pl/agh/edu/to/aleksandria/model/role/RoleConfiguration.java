package pl.agh.edu.to.aleksandria.model.role;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class RoleConfiguration {

    RoleRepository roleRepository;

    public RoleConfiguration(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

//    @PostConstruct
//    private void initRoles() {
//        Role readerRole = new Role("reader");
//        Role librarianRole = new Role("librarian");
//        Role adminRole = new Role("admin");
//
//        roleRepository.saveAll(List.of(readerRole, librarianRole, adminRole));
//    }
}
