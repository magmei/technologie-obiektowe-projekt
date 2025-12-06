package pl.agh.edu.to.aleksandria;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import pl.agh.edu.to.aleksandria.book.Book;
import pl.agh.edu.to.aleksandria.book.BookRepository;
import pl.agh.edu.to.aleksandria.role.Role;
import pl.agh.edu.to.aleksandria.role.RoleRepository;
import pl.agh.edu.to.aleksandria.user.User;
import pl.agh.edu.to.aleksandria.user.UserRepository;

@Configuration
public class TestConfiguration {

    UserRepository userRepository;
    RoleRepository roleRepository;
    BookRepository bookRepository;

    public TestConfiguration(UserRepository userRepository, RoleRepository roleRepository, BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bookRepository = bookRepository;
    }

    @PostConstruct
    private void initUsers() {
        Role testRole = new Role("czytelnik");
        roleRepository.save(testRole);

        User testUser = new User("Jan", "Testowy", "ul. Tymczasowa 1/2", "jan.testowy@poczta.pl", testRole);
        userRepository.save(testUser);

        Book testBook = new Book(1);
        bookRepository.save(testBook);

        System.out.println(userRepository.findAll());
    }

}
