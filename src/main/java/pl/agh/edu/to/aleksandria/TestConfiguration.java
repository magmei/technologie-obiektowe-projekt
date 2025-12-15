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
        Role adminRole = roleRepository.findByName("admin").get();

        List<User> readers = List.of(
                new User("Adrian", "Suliga", "ul. Tymczasowa 1/2", "adrian.suliga@poczta.pl", passwordEncoder.encode("suliga"), readerRole),
                new User("Jakub", "Więcek", "ul. Kawiory 34", "jakub.więcek@poczta.pl", passwordEncoder.encode("wiecek"), readerRole),
                new User("Filip", "Wolski", "ul. Sienkiewicza 21", "filip.wolski@poczta.pl", passwordEncoder.encode("wolski"), readerRole)
        );
        userRepository.saveAll(readers);

        List<User> librarians = List.of(
                new User("Adam", "Nowak", "ul. Kawiory 21", "adam.nowak@alexandria.com", passwordEncoder.encode("nowak"), librarianRole),
                new User("Jan", "Kowalski", "ul. Słowackiego 14", "jan.kowalski@alexandria.com", passwordEncoder.encode("kowalski"), librarianRole)
        );
        userRepository.saveAll(librarians);

        User admin = new User("Marta", "Admin", "ul. Admina 7", "admin@alexandria.com", passwordEncoder.encode("adminpass"), adminRole);
        userRepository.save(admin);

        List<Genre> genres = List.of(
                new Genre("Fantastyka"), new Genre("Historia"), new Genre("Horror"),
                new Genre("Romans"), new Genre("Science-Fiction"), new Genre("Tajemnica"),
                new Genre("Dystopia"), new Genre("Literatura Klasyczna"), new Genre("Biografia")
        );
        genreRepository.saveAll(genres);

        List<Title> titles = List.of(
                new Title("Rok 1984", "George Orwell", List.of(genres.get(6))),
                new Title("Światło, którego nie widać", "Anthony Doerr", List.of(genres.get(1))),
                new Title("Przeminęło z wiatrem", "Margaret Mitchell", List.of(genres.get(1), genres.get(3))),
                new Title("Drużyna Pierścienia", "John Ronald Reuel Tolkien", List.of(genres.getFirst())),
                new Title("Pan Tadeusz", "Adam Mickiewicz", List.of(genres.get(7)))
        );
        titleRepository.saveAll(titles);

        List<Book> books = List.of(
                new Book(titles.getFirst(), true),
                new Book(titles.getFirst(), true),
                new Book(titles.getFirst(), true),
                new Book(titles.get(1), true),
                new Book(titles.get(2), true),
                new Book(titles.get(2), true),
                new Book(titles.get(2), true),
                new Book(titles.get(2), true),
                new Book(titles.get(2), true),
                new Book(titles.get(3), true),
                new Book(titles.get(3), true),
                new Book(titles.getLast(), true),
                new Book(titles.getLast(), true)
        );
        bookRepository.saveAll(books);

        System.out.println(userRepository.findAll());
        System.out.println(bookRepository.findAll());
        System.out.println(roleRepository.findAll());
    }

}
