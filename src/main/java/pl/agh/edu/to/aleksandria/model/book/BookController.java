package pl.agh.edu.to.aleksandria.model.book;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    // GET /books/all
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('READER', 'LIBRARIAN', 'ADMIN')")
    public List<Book> getAllBooks() {
        return  bookService.getAllBooks();
    }

    // GET /books/search/by_id?id=
    @GetMapping("/search/by_id")
    @PreAuthorize("hasAnyRole('READER', 'LIBRARIAN', 'ADMIN')")
    public ResponseEntity<Object> getBookById(@RequestParam Integer id) {
        return this.optionalToResponseEntity(
                bookService.getBookById(id),
                HttpStatus.NOT_FOUND,
                "No book with this ID found"
        );
    }

    // GET /books/search/by_availability?available=
    @GetMapping("/search/by_availability")
    @PreAuthorize("hasAnyRole('READER', 'LIBRARIAN', 'ADMIN')")
    public List<Book> getBooksByAvailability(@RequestParam Boolean availability) {
        return bookService.getBooksByAvailability(availability);
    }

    // GET /books/search/by_name?title=
    @GetMapping("/search/by_name")
    @PreAuthorize("hasAnyRole('READER', 'LIBRARIAN', 'ADMIN')")
    public List<Book> getAvailableBooksByTitle(@RequestParam String title) {
        return bookService.getAvailableBooksByTitleName(title);
    }

    // POST /books/create?title_id=
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public ResponseEntity<Object> createBook(Integer title_id) {
        return this.optionalToResponseEntity(
                bookService.createBook(title_id),
                HttpStatus.BAD_REQUEST,
                "Could not create book for this title"
        );
    }

    // PUT /books/change_availability?id=?available=
    @PutMapping("/update_availability")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public ResponseEntity<Object> changeAvailability(@RequestParam Integer id, @RequestParam boolean availability) {
        return this.optionalToResponseEntity(
                bookService.changeAvailability(id, availability),
                HttpStatus.BAD_REQUEST,
                "Could not update book with this id"
        );
    }

    // DELETE /books/delete?id=
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public ResponseEntity<Object> deleteBook(@RequestParam Integer id) {
        boolean deleted = bookService.deleteBook(id);

        if (deleted) {
            return ResponseEntity.ok(Map.of("message", "Book deleted"));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Book not found"));
    }

    private ResponseEntity<Object> optionalToResponseEntity(Optional<Book> book, HttpStatus status, String error) {
        return book.<ResponseEntity<Object>>map(ResponseEntity::ok).orElse(
                ResponseEntity.status(status).body(Map.of("error", error)));
    }
}
