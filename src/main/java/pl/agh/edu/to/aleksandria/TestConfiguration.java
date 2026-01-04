package pl.agh.edu.to.aleksandria;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.agh.edu.to.aleksandria.model.book.Book;
import pl.agh.edu.to.aleksandria.model.book.BookRepository;
import pl.agh.edu.to.aleksandria.model.genre.Genre;
import pl.agh.edu.to.aleksandria.model.genre.GenreRepository;
import pl.agh.edu.to.aleksandria.model.rental.Rental;
import pl.agh.edu.to.aleksandria.model.rental.RentalRepository;
import pl.agh.edu.to.aleksandria.model.rental.RentalService;
import pl.agh.edu.to.aleksandria.model.rental.dtos.CreateRentalRequest;
import pl.agh.edu.to.aleksandria.model.role.Role;
import pl.agh.edu.to.aleksandria.model.role.RoleRepository;
import pl.agh.edu.to.aleksandria.model.title.Title;
import pl.agh.edu.to.aleksandria.model.title.TitleRepository;
import pl.agh.edu.to.aleksandria.model.user.User;
import pl.agh.edu.to.aleksandria.model.user.UserRepository;

import java.util.List;
import java.util.Optional;

@Configuration
@AllArgsConstructor
public class TestConfiguration {

    private final PasswordEncoder passwordEncoder;
    UserRepository userRepository;
    RoleRepository roleRepository;
    BookRepository bookRepository;
    GenreRepository genreRepository;
    TitleRepository titleRepository;
    RentalRepository rentalRepository;

    RentalService rentalService;

    @PostConstruct
    private void initDB() {
        System.out.println("Users: " + userRepository.findAll());
        System.out.println("Books: " + bookRepository.findAll());
        System.out.println("User roles: " + roleRepository.findAll());

        Optional<Rental> rentalOpt = rentalService.createRental(new CreateRentalRequest(1, 1, 14));
        for (Rental rental : rentalRepository.findAll()) {
            System.out.println("Rental: " + rental);
        }
        rentalService.returnRental(rentalOpt.get().getId());
        for (Rental rental : rentalRepository.findAll()) {
            System.out.println("Rental: " + rental);
        }
        rentalService.deleteRental(rentalOpt.get().getId());
    }
}
