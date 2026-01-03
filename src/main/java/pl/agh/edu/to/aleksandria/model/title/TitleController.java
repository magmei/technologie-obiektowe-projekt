package pl.agh.edu.to.aleksandria.model.title;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.agh.edu.to.aleksandria.model.title.dtos.CreateTitleRequest;
import pl.agh.edu.to.aleksandria.model.title.dtos.UpdateTitleRequest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/titles")
public class TitleController {

    private final TitleService titleService;

    // GET /titles/all
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('READER', 'LIBRARIAN', 'ADMIN')")
    public List<Title> getAllTitles() {
        return titleService.getAllTitles();
    }

    // GET /titles/search/by_id?id=
    @GetMapping("/search/by_id")
    @PreAuthorize("hasAnyRole('READER', 'LIBRARIAN', 'ADMIN')")
    public ResponseEntity<Object> getTitleById(@RequestParam Integer id) {
        return this.optionalToResponseEntity(
                titleService.getTitleById(id),
                HttpStatus.NOT_FOUND,
                "No Title with this ID found"
        );
    }

    // GET /titles/search/by_name?name=
    @GetMapping("/search/by_name")
    @PreAuthorize("hasAnyRole('READER', 'LIBRARIAN', 'ADMIN')")
    public ResponseEntity<Object> getTitleByName(@RequestParam String name) {
        return this.optionalToResponseEntity(
                titleService.getTitleByName(name),
                HttpStatus.NOT_FOUND,
                "No Title with this name found"
        );
    }

    // GET /titles/search/by_author?author=
    @GetMapping("/search/by_author")
    @PreAuthorize("hasAnyRole('READER', 'LIBRARIAN', 'ADMIN')")
    public List<Title> getTitleByAuthor(@RequestParam String author) {
        return titleService.getTitleByAuthor(author);
    }

    // GET /titles/search/by_genre?genre=
    @GetMapping("/search/by_genre")
    @PreAuthorize("hasAnyRole('READER', 'LIBRARIAN', 'ADMIN')")
    public List<Title> getTitleByGenre(@RequestParam String genre) {
        return titleService.getTitleByGenre(genre);
    }

    // POST /titles/create
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public ResponseEntity<Object> createTitle(@RequestBody CreateTitleRequest request) {
        return this.optionalToResponseEntity(
                titleService.createTitle(request),
                HttpStatus.BAD_REQUEST,
                "Could not create new title"
        );
    }

    // PUT /titles/update
    @PostMapping("/update")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public ResponseEntity<Object> updateTitle(@RequestBody UpdateTitleRequest request) {
        return this.optionalToResponseEntity(
                titleService.updateTitle(request),
                HttpStatus.BAD_REQUEST,
                "Could not update this title"
        );
    }

    // DELETE /titles/delete?id=
    @PostMapping("/delete")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public ResponseEntity<Object> deleteTitle(@RequestParam Integer id) {
        boolean deleted = titleService.deleteTitle(id);

        if (deleted) {
            return ResponseEntity.ok(Map.of("message", "Title deleted"));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Title not found"));
    }

    private ResponseEntity<Object> optionalToResponseEntity(Optional<Title> book, HttpStatus status, String error) {
        return book.<ResponseEntity<Object>>map(ResponseEntity::ok).orElse(
                ResponseEntity.status(status).body(Map.of("error", error)));
    }
}
