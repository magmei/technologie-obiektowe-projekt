package pl.agh.edu.to.aleksandria;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import pl.agh.edu.to.aleksandria.book.Book;
import pl.agh.edu.to.aleksandria.book.BookRepository;
import pl.agh.edu.to.aleksandria.genre.Genre;
import pl.agh.edu.to.aleksandria.genre.GenreRepository;
import pl.agh.edu.to.aleksandria.role.Role;
import pl.agh.edu.to.aleksandria.role.RoleRepository;
import pl.agh.edu.to.aleksandria.title.Title;
import pl.agh.edu.to.aleksandria.title.TitleRepository;
import pl.agh.edu.to.aleksandria.user.User;
import pl.agh.edu.to.aleksandria.user.UserRepository;

import java.util.List;

@Configuration
public class TestConfiguration {

    UserRepository userRepository;
    RoleRepository roleRepository;
    BookRepository bookRepository;
    GenreRepository genreRepository;
    TitleRepository titleRepository;

    public TestConfiguration(UserRepository userRepository, RoleRepository roleRepository, BookRepository bookRepository, GenreRepository genreRepository, TitleRepository titleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bookRepository = bookRepository;
        this.genreRepository = genreRepository;
        this.titleRepository = titleRepository;
    }

    @PostConstruct
    private void initUsers() {
        Role testRole = new Role("czytelnik");
        roleRepository.save(testRole);

        User testUser = new User("Jan", "Testowy", "ul. Tymczasowa 1/2", "jan.testowy@poczta.pl", testRole);
        userRepository.save(testUser);

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
    }

}
