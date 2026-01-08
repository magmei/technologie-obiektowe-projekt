package pl.agh.edu.to.aleksandria.model.rental;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.agh.edu.to.aleksandria.model.book.Book;
import pl.agh.edu.to.aleksandria.model.book.BookService;
import pl.agh.edu.to.aleksandria.model.rental.dtos.CreateRentalRequest;
import pl.agh.edu.to.aleksandria.model.user.User;
import pl.agh.edu.to.aleksandria.model.user.UserService;
import pl.agh.edu.to.aleksandria.notifications.MailService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RentalService {

    private final RentalRepository rentalRepository;
    private final UserService userService;
    private final BookService bookService;
    private final MailService mailService;

    @PostConstruct
    public void onServiceStarted() {
        System.out.println("Rental service started");
    }

    @PreDestroy
    public void onServiceStopped() {
        System.out.println("Rental service destroyed");
    }


    private double calculateFee(Rental rental, Double dailyRate) {
        long daysLate = ChronoUnit.DAYS.between(rental.getDue(), rental.getReturnedOn());
        if (daysLate > 0) {
            return daysLate * dailyRate;
        }
        return 0.0;
    }

    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    public List<Rental> getRentalsByBook(long bookId) {
        return rentalRepository.findByBook_ItemId(bookId);
    }

    public List<Rental> getRentalsByTitle(long titleId) {
        return rentalRepository.findByBook_Title_Id(titleId);
    }

    public Optional<Rental> getRentalById(Integer id) {
        return Optional.ofNullable(rentalRepository.findById(id).orElse(null));
    }

    public List<Rental> getRentalsByUser(long userId) {
        return rentalRepository.findByUser_Id(userId);
    }

    public List<Rental> getRentalsByDateRange(LocalDate startDate, LocalDate endDate) {
        return rentalRepository.findAll().stream()
                .filter(rental -> !rental.getRentedOn().isAfter(endDate) &&
                        !rental.getRentedOn().isBefore(startDate))
                .toList();
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

        mailService.sendOnRentalEmail(user.get(), book.get(), rental);

        return Optional.of(rentalRepository.save(rental));
    }

    public Optional<Rental> returnRental(int rentalId) {
        Optional<Rental> rental = rentalRepository.findById(rentalId);
        if (rental.isEmpty() || rental.get().getReturnedOn() != null) {
            return Optional.empty();
        }
        rental.get().setReturnedOn(LocalDate.now());
        bookService.changeAvailability(rental.get().getBook().getItemId(), true); // make the book available again
        double fee = calculateFee(rental.get(), 2.5); // eventually should be configurable
        rental.get().setFee(fee);
        return Optional.of(rentalRepository.save(rental.get()));
    }

    public boolean deleteRental(int id) {
        Optional<Rental> rental = rentalRepository.findById(id);
        if (rental.isEmpty()) {
            return false;
        }

        bookService.changeAvailability(rental.get().getBook().getItemId(), true);

        rentalRepository.delete(rental.get());
        return true;
    }
}
