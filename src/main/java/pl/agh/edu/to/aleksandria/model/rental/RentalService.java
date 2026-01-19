package pl.agh.edu.to.aleksandria.model.rental;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.agh.edu.to.aleksandria.model.book.Book;
import pl.agh.edu.to.aleksandria.model.book.BookService;
import pl.agh.edu.to.aleksandria.model.queue.QueueService;
import pl.agh.edu.to.aleksandria.model.queue.dtos.QueueRequest;
import pl.agh.edu.to.aleksandria.model.rental.dtos.CreateRentalRequest;
import pl.agh.edu.to.aleksandria.model.title.Title;
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
    private final QueueService queueService;

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

    public boolean canCreateRental(int userId, int bookId) {
        // we allow creating rentals only if user/book exist, book is available and user has no overdue rentals
        // in addition, if the title of the book has a queue, the count of available books of that title is the
        // max position in the queue for a user to be able to rent a book of that title

        Optional<User> user = userService.getUserById(userId);
        if (user.isEmpty()) {
            return false;
        }
        Optional<Book> book = bookService.getBookById(bookId);
        if (book.isEmpty() || !book.get().isAvailable()) {
            return false;
        }

        Title title = book.get().getTitle();

        List<Rental> userRentals = rentalRepository.findByUser_Id(userId);
        for (Rental rental : userRentals) {
            if (rental.getReturnedOn() == null && rental.getDue().isBefore(LocalDate.now())) {
                return false; // user has an overdue rental
            }
        }

        List<User> queue = queueService.getUsersWaitingForTitle(title.getId());
        if (queue.isEmpty()) {
            // no queue, rental can be created
            return true;
        } else {
            // there is a queue, check user's position
            int position = queueService.getPositionInQueue(userId, title.getId());
            if (position == -1) {
                return false; // user not in queue
            }
            long availableBooksCount = bookService.getBooksByTitleId(title.getId()).stream()
                    .filter(Book::isAvailable)
                    .count();
            return position <= availableBooksCount; // user can rent only if their position is within available books
        }
    }

    @Transactional
    public Optional<Rental> createRental(CreateRentalRequest request) {
        if (request.getRentalDays() <= 0) {
            return Optional.empty();
        }

        int userId = request.getUserId();
        int bookId = request.getBookId();

        // those checks are redundant if canCreateRental is used before calling this method (and it should always be)
        // but the methods from service classes return Optionals
        Optional<User> user = userService.getUserById(userId);
        if (user.isEmpty()) {
            return Optional.empty();
        }
        Optional<Book> book = bookService.getBookById(bookId);
        if (book.isEmpty()) {
            return Optional.empty();
        }

        Title title = book.get().getTitle();

        Rental rental = new Rental(user.get(), book.get(), LocalDate.now(), LocalDate.now().plusDays(request.getRentalDays()), null);
        bookService.changeAvailability(book.get().getItemId(), false);

        if (!queueService.getUsersWaitingForTitle(title.getId()).isEmpty()) {
            // if there is a queue for the title of the rented book, remove the user from it
            queueService.removeUserFromQueue(new QueueRequest(userId, title.getId()));
        }

        mailService.sendOnRentalEmail(rental);

        return Optional.of(rentalRepository.save(rental));
    }

    @Transactional
    public Optional<Rental> returnRental(int rentalId) {
        Optional<Rental> rental = rentalRepository.findById(rentalId);
        if (rental.isEmpty() || rental.get().getReturnedOn() != null) {
            return Optional.empty();
        }
        rental.get().setReturnedOn(LocalDate.now());
        bookService.changeAvailability(rental.get().getBook().getItemId(), true); // make the book available again
        double fee = calculateFee(rental.get(), 2.5); // eventually should be configurable
        rental.get().setFee(fee);

        mailService.sendOnRentalReturnedEmail(rental.get());

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
