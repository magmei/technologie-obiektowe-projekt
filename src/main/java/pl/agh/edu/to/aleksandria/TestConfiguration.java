package pl.agh.edu.to.aleksandria;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.agh.edu.to.aleksandria.model.book.Book;
import pl.agh.edu.to.aleksandria.model.book.BookRepository;
import pl.agh.edu.to.aleksandria.model.genre.Genre;
import pl.agh.edu.to.aleksandria.model.genre.GenreRepository;
import pl.agh.edu.to.aleksandria.model.role.Role;
import pl.agh.edu.to.aleksandria.model.role.RoleRepository;
import pl.agh.edu.to.aleksandria.model.title.Title;
import pl.agh.edu.to.aleksandria.model.title.TitleRepository;
import pl.agh.edu.to.aleksandria.model.user.User;
import pl.agh.edu.to.aleksandria.model.user.UserRepository;

import java.util.List;

@Configuration
@DependsOn("roleConfiguration")
public class TestConfiguration {

    private final PasswordEncoder passwordEncoder;
    UserRepository userRepository;
    RoleRepository roleRepository;
    BookRepository bookRepository;
    GenreRepository genreRepository;
    TitleRepository titleRepository;

    public TestConfiguration(UserRepository userRepository, RoleRepository roleRepository, BookRepository bookRepository, GenreRepository genreRepository, TitleRepository titleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bookRepository = bookRepository;
        this.genreRepository = genreRepository;
        this.titleRepository = titleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    private void initDB() {
        Role readerRole = roleRepository.findByName("reader").get();
        Role librarianRole = roleRepository.findByName("librarian").get();

        User testUser = new User("Jan", "Testowy", "ul. Tymczasowa 1/2", "jan.testowy@poczta.pl", passwordEncoder.encode("test1234"), readerRole);
        User testEmployee = new User("Adam", "Bibliotekarz", "ul. Kawiory 21", "adam.biliot@gmail.com", passwordEncoder.encode("test1234"), librarianRole);
        userRepository.saveAll(List.of(testUser, testEmployee));

        Genre dystopia = new Genre("Dystopia");
        Genre political = new Genre("Political");
        genreRepository.save(dystopia);
        genreRepository.save(political);

        Title testTitle = new Title("Rok 1984", "George Orwell", List.of(dystopia, political));
        titleRepository.save(testTitle);

        Book testBook = new Book(testTitle, false);
        bookRepository.save(testBook);

        System.out.println(userRepository.findAll());
        System.out.println(bookRepository.findAll());
        System.out.println(roleRepository.findAll());
    }

}
