package pl.agh.edu.to.aleksandria;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import pl.agh.edu.to.aleksandria.role.Role;
import pl.agh.edu.to.aleksandria.role.RoleRepository;
import pl.agh.edu.to.aleksandria.user.User;
import pl.agh.edu.to.aleksandria.user.UserRepository;

@Configuration
public class UserConfiguration {

    UserRepository userRepository;
    RoleRepository roleRepository;

    public UserConfiguration(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    // Create a test user and print it
    @PostConstruct
    private void initUsers() {
        Role testRole = new Role("czytelnik");
        roleRepository.save(testRole);
        User testUser = new User("Jan", "Testowy", "ul. Tymczasowa 1/2", "jan.testowy@poczta.pl", testRole);
        userRepository.save(testUser);
        System.out.println(userRepository.findAll());
    }

}
