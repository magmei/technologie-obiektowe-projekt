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

        Optional<Rental> rentalOpt = rentalService.createRental(new CreateRentalRequest(1, 1, 14));
        for (Rental rental : rentalRepository.findAll()) {
            System.out.println("Rental: " + rental);
        }
        rentalService.returnRental(rentalOpt.get().getId());
        for (Rental rental : rentalRepository.findAll()) {
            System.out.println("Rental: " + rental);
        }
        rentalService.deleteRental(rentalOpt.get().getId());

        queueService.addUserToQueue(new QueueRequest(1, 1));
        queueService.addUserToQueue(new QueueRequest(2, 1));
        queueService.addUserToQueue(new QueueRequest(3, 2));
        queueService.addUserToQueue(new QueueRequest(1, 2));

        System.out.println("queue: user 1 for book 1: " + queueService.getPositionInQueue(1, 1));
        System.out.println("queue: user 2 for book 1: " + queueService.getPositionInQueue(2, 1));
        System.out.println("queue: user 1 for book 2: " + queueService.getPositionInQueue(1, 2));

        System.out.println("Users in queue for book 1: " + queueService.getUsersWaitingForTitle(1).size());
        System.out.println("Users in queue for book 2: " + queueService.getUsersWaitingForTitle(2).size());

        queueService.removeUserFromQueue(new QueueRequest(1, 1));
        queueService.removeUserFromQueue(new QueueRequest(1, 2));

        System.out.println("queue: user 2 for book 1: " + queueService.getPositionInQueue(2, 1));
        System.out.println("queue: user 3 for book 2: " + queueService.getPositionInQueue(3, 2));

        System.out.println("Users in queue for book 1: " + queueService.getUsersWaitingForTitle(1).size());
        System.out.println("Users in queue for book 2: " + queueService.getUsersWaitingForTitle(2).size());

        queueService.removeUserFromQueue(new QueueRequest(2 , 1));
        queueService.removeUserFromQueue(new QueueRequest(3 , 2));

        System.out.println("Reviews of book 1: " + reviewRepository.findByTitleId(1));
    }
}
