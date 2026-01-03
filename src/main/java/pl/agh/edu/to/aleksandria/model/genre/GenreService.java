package pl.agh.edu.to.aleksandria.model.genre;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.agh.edu.to.aleksandria.model.genre.dtos.UpdateGenreRequest;
import pl.agh.edu.to.aleksandria.model.title.Title;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;

    @PostConstruct
    public void onServiceStarted() {
        System.out.println("Genre service started");
    }

    @PreDestroy
    public void onServiceStopped() {
        System.out.println("Genre service stopped");
    }

    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    public Optional<Genre> getGenreById(Integer id) {
        return genreRepository.findById(id);
    }

    public Optional<Genre> getGenreByName(String name) {
        return genreRepository.findByName(name);
    }

    public Optional<Genre> createGenre(String name) {
        Optional<Genre> existingGenre = genreRepository.findByName(name);
        if (existingGenre.isPresent()) {
            return Optional.empty();
        }

        Genre genre = new Genre(name);
        return Optional.of(genreRepository.save(genre));
    }

    public Optional<Genre> updateGenre(UpdateGenreRequest updateGenreRequest) {
        Optional<Genre> genre = genreRepository.findById(updateGenreRequest.getGenreId());
        if (genre.isEmpty()) {
            return Optional.empty();
        }

        Genre updatedGenre = genre.get();

        if (!Objects.equals(updatedGenre.getName(), updateGenreRequest.getGenreName())) {
            updatedGenre.setName(updateGenreRequest.getGenreName());
        }

        for (Title new_title : updateGenreRequest.getTitlesToAdd()) {
            updatedGenre.addTitle(new_title);
        }

        for (Title old_title : updateGenreRequest.getTitlesToDelete()) {
            updatedGenre.removeTitle(old_title);
        }

        return Optional.of(genreRepository.save(updatedGenre));
    }

    public boolean deleteGenre(Integer id) {
        Optional<Genre> genre = genreRepository.findById(id);
        if (genre.isEmpty()) {
            return false;
        }

        genreRepository.deleteById(id);
        return true;
    }
}
