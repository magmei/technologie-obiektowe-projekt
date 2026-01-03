package pl.agh.edu.to.aleksandria.model.genre;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.agh.edu.to.aleksandria.model.genre.dtos.UpdateGenreRequest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/genres")
public class GenreController {

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    // GET /genres/all
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('READER', 'LIBRARIAN', 'ADMIN')")
    public List<Genre> getAllGenres() {
        return genreService.getAllGenres();
    }

    // GET /genres/search/by_id?id=
    @GetMapping("/search/by_id")
    @PreAuthorize("hasAnyRole('READER', 'LIBRARIAN', 'ADMIN')")
    public ResponseEntity<Object> getGenreById(@RequestParam Integer id) {
        return this.optionalToResponseEntity(
                genreService.getGenreById(id),
                HttpStatus.NOT_FOUND,
                "No genre with this ID found"
        );
    }

    // GET /genres/search/by_name?name=
    @GetMapping("/search/by_name")
    @PreAuthorize("hasAnyRole('READER', 'LIBRARIAN', 'ADMIN')")
    public ResponseEntity<Object> getGenreByName(@RequestParam String name) {
        return this.optionalToResponseEntity(
                genreService.getGenreByName(name),
                HttpStatus.NOT_FOUND,
                "No genre with this name found"
        );
    }

    // POST /genres/create?name=
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public ResponseEntity<Object> createGenre(@RequestParam String name) {
        return this.optionalToResponseEntity(
                genreService.createGenre(name),
                HttpStatus.BAD_REQUEST,
                "Could not create genre with this name"
        );
    }

    // PUT /genres/update
    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public ResponseEntity<Object> updateGenre(@RequestBody UpdateGenreRequest request) {
        return this.optionalToResponseEntity(
                genreService.updateGenre(request),
                HttpStatus.BAD_REQUEST,
                "Could not update this genre"
        );
    }

    // DELETE /genres/delete?id=
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public ResponseEntity<Object> deleteGenre(@RequestParam Integer id) {
        boolean deleted = genreService.deleteGenre(id);

        if (deleted) {
            return ResponseEntity.ok(Map.of("message", "Genre deleted"));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Genre not found"));
    }

    private ResponseEntity<Object> optionalToResponseEntity(Optional<Genre> genre,
                                                            HttpStatus status,
                                                            String errorMessage)
    {
        return genre.<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(status).body(Map.of("error", errorMessage)));
    }
}
