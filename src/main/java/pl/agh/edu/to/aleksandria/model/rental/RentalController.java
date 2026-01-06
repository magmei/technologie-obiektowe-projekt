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
    public List<Rental> getAllRentals() {
        return rentalService.getAllRentals();
    }

    // GET /rentals/search/by_user?user_id=
    @GetMapping("/search/by_user")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public List<Rental> getRentalsByUser(@RequestParam long user_id) {
        return rentalService.getRentalsByUser(user_id);
    }

    // GET /rentals/search/by_book?book_id=
    @GetMapping("/search/by_book")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public List<Rental> getRentalsByBook(@RequestParam long book_id) {
        return rentalService.getRentalsByBook(book_id);
    }

    // GET /rentals/search/by_date_range?start_date=&end_date=
    @GetMapping("/search/by_date_range")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public List<Rental> getRentalsByDateRange(@RequestParam LocalDate start_date, @RequestParam LocalDate end_date) {
        return rentalService.getRentalsByDateRange(start_date, end_date);
    }

    // GET /rentals/search/by_date?date=
    @GetMapping("/search/by_date")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public List<Rental> getRentalsByDate(@RequestParam LocalDate date) {
        return rentalService.getRentalsByDateRange(date, date);
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

    private ResponseEntity<Object> optionalToResponseEntity(Optional<Rental> rental, HttpStatus status, String err) {
        return rental.<ResponseEntity<Object>>map(ResponseEntity::ok).orElse(
                ResponseEntity.status(status).body(Map.of("error", err)));
    }
}
