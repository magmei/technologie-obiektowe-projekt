package pl.agh.edu.to.aleksandria.model.rental;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.agh.edu.to.aleksandria.model.rental.dtos.CreateRentalRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/rentals")
public class RentalController {

    private final RentalService rentalService;

    // =====================
    // *** GET endpoints ***
    // =====================

    // GET /rentals/search/by_id?id=
    @GetMapping("/search/by_id")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public ResponseEntity<Object> getRentalById(@RequestParam int id) {
        return this.optionalToResponseEntity(
                rentalService.getRentalById(id),
                HttpStatus.NOT_FOUND,
                "No rental with this ID found"
        );
    }

    // GET /rentals/all
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public ResponseEntity<Object> getAllRentals() {
        return this.optionalToResponseEntity(
                rentalService.getAllRentals(),
                HttpStatus.NOT_FOUND,
                "No rentals found"
        );
    }

    // GET /rentals/search/by_user?user_id=
    @GetMapping("/search/by_user")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public ResponseEntity<Object> getRentalsByUser(@RequestParam long user_id) {
        return this.optionalToResponseEntity(
                rentalService.getRentalsByUser(user_id),
                HttpStatus.NOT_FOUND,
                "No rentals for this user ID found"
        );
    }

    // GET /rentals/search/by_book?book_id=
    @GetMapping("/search/by_book")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public ResponseEntity<Object> getRentalsByBook(@RequestParam long book_id) {
        return this.optionalToResponseEntity(
                rentalService.getRentalsByBook(book_id),
                HttpStatus.NOT_FOUND,
                "No rentals for this book ID found"
        );
    }

    // GET /rentals/search/by_title?title_id=
    @GetMapping("/search/by_title")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public ResponseEntity<Object> getRentalsByTitle(@RequestParam long title_id) {
        return this.optionalToResponseEntity(
                rentalService.getRentalsByTitle(title_id),
                HttpStatus.NOT_FOUND,
                "No rentals for this title ID found"
        );
    }

    // GET /rentals/search/by_date_range?start_date=&end_date=
    @GetMapping("/search/by_date_range")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public ResponseEntity<Object> getRentalsByDateRange(@RequestParam LocalDate start_date, @RequestParam LocalDate end_date) {
        return this.optionalToResponseEntity(
                rentalService.getRentalsByDateRange(start_date, end_date),
                HttpStatus.NOT_FOUND,
                "No rentals in this date range found"
        );
    }

    // GET /rentals/search/by_date?date=
    @GetMapping("/search/by_date")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public ResponseEntity<Object> getRentalsByDate(@RequestParam LocalDate date) {
        return this.optionalToResponseEntity(
                rentalService.getRentalsByDateRange(date, date),
                HttpStatus.NOT_FOUND,
                "No rentals on this date found"
        );
    }

    // TODO more endpoints as needed

    // ======================
    // *** POST endpoints ***
    // ======================

    // POST /rentals/create
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public ResponseEntity<Object> createRental(CreateRentalRequest request) {
        return this.optionalToResponseEntity(
                rentalService.createRental(request),
                HttpStatus.BAD_REQUEST,
                "Could not create rental with given data"
        );
    }

    // POST /rentals/return?rental_id=
    @PostMapping("/return")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public ResponseEntity<Object> returnRental(@RequestParam int rental_id) {
        return this.optionalToResponseEntity(
                rentalService.returnRental(rental_id),
                HttpStatus.BAD_REQUEST,
                "Could not return rental with given id"
        );
    }

    // ========================
    // *** DELETE endpoints ***
    // ========================

    // DELETE /rentals/delete?id=
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public ResponseEntity<Object> deleteRental(@RequestParam int id) {
        boolean deleted = rentalService.deleteRental(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Could not delete rental with given id"));
        }
    }

    private <T> ResponseEntity<Object> optionalToResponseEntity(List<T> list, HttpStatus httpStatus, String s) {
        if (list.isEmpty()) {
            return new ResponseEntity<>(s, httpStatus);
        } else {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    private ResponseEntity<Object> optionalToResponseEntity(Optional<Rental> rental, HttpStatus status, String err) {
        return rental.<ResponseEntity<Object>>map(ResponseEntity::ok).orElse(
                ResponseEntity.status(status).body(Map.of("error", err)));
    }
}
