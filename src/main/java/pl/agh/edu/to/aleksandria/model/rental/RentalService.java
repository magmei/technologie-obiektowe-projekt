package pl.agh.edu.to.aleksandria.model.rental;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.agh.edu.to.aleksandria.model.book.Book;
import pl.agh.edu.to.aleksandria.model.book.BookService;
import pl.agh.edu.to.aleksandria.model.rental.dtos.CreateRentalRequest;
import pl.agh.edu.to.aleksandria.model.title.Title;
import pl.agh.edu.to.aleksandria.model.user.User;
import pl.agh.edu.to.aleksandria.model.user.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RentalService {

    private final RentalRepository rentalRepository;
    private final UserService userService;
    private final BookService bookService;

    @PostConstruct
    public void onServiceStarted() {
        System.out.println("Rental service started");
    }

    @PreDestroy
    public void onServiceStopped() {
        System.out.println("Rental service destroyed");
    }

    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    public List<Rental> getRentalsByBook(Book book) {
        return rentalRepository.findByBook(book);
    }

    public List<Rental> getRentalsByTitle(Title title) {
        return null;
        // TODO
    }

    public Rental getRentalById(Integer id) {
        return rentalRepository.findById(id).orElse(null);
    }

    public Rental getRentalsByUser(Long userId) {
        return rentalRepository.findByUser_Id(userId);
    }

    public Optional<Rental> createRental(CreateRentalRequest request) {
        if (request.getRentalDays() <= 0) {
            return Optional.empty();
        }
        Optional<User> user = userService.getUserById(request.getUserId());
        if (user.isEmpty()) {
            return Optional.empty();
        }
        Optional<Book> book = bookService.getBookById(request.getBookId());
        if (book.isEmpty() || !book.get().isAvailable()) {
            return Optional.empty();
        }

        Rental rental = new Rental(user.get(), book.get(), LocalDate.now(), LocalDate.now().plusDays(request.getRentalDays()), null);
        bookService.changeAvailability(book.get().getItemId(), false);

        return Optional.of(rentalRepository.save(rental));
    }

    public Optional<Rental> returnRental(Integer rentalId) {
        Optional<Rental> rental = rentalRepository.findById(rentalId);
        if (rental.isEmpty() || rental.get().getReturnedOn() != null) {
            return Optional.empty();
        }

        rental.get().setReturnedOn(LocalDate.now());
        bookService.changeAvailability(rental.get().getBook().getItemId(), true);
        return Optional.of(rentalRepository.save(rental.get()));
    }

    public boolean deleteRental(Integer id) {
        Optional<Rental> rental = rentalRepository.findById(id);
        if (rental.isEmpty()) {
            return false;
        }

        bookService.changeAvailability(rental.get().getBook().getItemId(), true);

        rentalRepository.delete(rental.get());
        return true;
    }
}
