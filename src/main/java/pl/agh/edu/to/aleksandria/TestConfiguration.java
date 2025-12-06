package pl.agh.edu.to.aleksandria;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
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
@DependsOn("roleConfiguration")
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
    private void initDB() {
        Role readerRole = roleRepository.findByName("reader").get();
        Role librarianRole = roleRepository.findByName("librarian").get();

        User testUser = new User("Jan", "Testowy", "ul. Tymczasowa 1/2", "jan.testowy@poczta.pl", readerRole);
        User testEmployee = new User("Adam", "Bibliotekarz", "ul. Kawiory 21", "adam.biliot@gmail.com", librarianRole);
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
