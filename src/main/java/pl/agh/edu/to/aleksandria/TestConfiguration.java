package pl.agh.edu.to.aleksandria;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.agh.edu.to.aleksandria.model.book.BookRepository;
import pl.agh.edu.to.aleksandria.model.genre.GenreRepository;
import pl.agh.edu.to.aleksandria.model.queue.QueueRepository;
import pl.agh.edu.to.aleksandria.model.queue.QueueService;
import pl.agh.edu.to.aleksandria.model.queue.dtos.QueueRequest;
import pl.agh.edu.to.aleksandria.model.rental.Rental;
import pl.agh.edu.to.aleksandria.model.rental.RentalRepository;
import pl.agh.edu.to.aleksandria.model.rental.RentalService;
import pl.agh.edu.to.aleksandria.model.rental.dtos.CreateRentalRequest;
import pl.agh.edu.to.aleksandria.model.review.ReviewRepository;
import pl.agh.edu.to.aleksandria.model.role.RoleRepository;
import pl.agh.edu.to.aleksandria.model.title.TitleRepository;
import pl.agh.edu.to.aleksandria.model.user.UserRepository;

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
    QueueRepository queueRepository;
    ReviewRepository reviewRepository;

    RentalService rentalService;
    QueueService queueService;

    @PostConstruct
    private void initDB() {
        System.out.println("Users: " + userRepository.findAll());
        System.out.println("Books: " + bookRepository.findAll());
        System.out.println("User roles: " + roleRepository.findAll());

        for (Rental rental : rentalRepository.findAll()) {
            System.out.println("Rental: " + rental);
        }
        System.out.println("Rentals for title 1: " + rentalService.getRentalsByTitle(1));

        System.out.println("Reviews of book 1: " + reviewRepository.findByTitleId(1));
    }
}
