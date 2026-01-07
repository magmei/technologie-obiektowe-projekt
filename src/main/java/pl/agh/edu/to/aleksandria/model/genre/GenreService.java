package pl.agh.edu.to.aleksandria.model.genre;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.agh.edu.to.aleksandria.model.genre.dtos.UpdateGenreRequest;
import pl.agh.edu.to.aleksandria.model.title.Title;
import pl.agh.edu.to.aleksandria.model.title.TitleRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;
    private final TitleRepository titleRepository;

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

    @Transactional
    public Optional<Genre> updateGenre(UpdateGenreRequest updateGenreRequest) {
        Optional<Genre> genre = genreRepository.findById(updateGenreRequest.getGenreId());
        if (genre.isEmpty()) {
            return Optional.empty();
        }

        Genre updatedGenre = genre.get();

        List<Title> titlesToAdd = titleRepository.findAllById(updateGenreRequest.getTitlesToAdd());
        List<Title> titlesToDelete = titleRepository.findAllById(updateGenreRequest.getTitlesToDelete());

        if (titlesToAdd.size() < updateGenreRequest.getTitlesToAdd().size() ||
            titlesToDelete.size() < updateGenreRequest.getTitlesToDelete().size()) {
            return Optional.empty();
        }

        updatedGenre.setName(updateGenreRequest.getGenreName());

        for (Title new_title : titlesToAdd) {
            updatedGenre.addTitle(new_title);
            new_title.addGenre(updatedGenre);
        }

        for (Title old_title : titlesToDelete) {
            updatedGenre.removeTitle(old_title);
            old_title.removeGenre(updatedGenre);
        }

        return Optional.of(updatedGenre);
    }

    @Transactional
    public boolean deleteGenre(Integer id) {
        Optional<Genre> genre_opt = genreRepository.findById(id);
        if (genre_opt.isEmpty()) {
            return false;
        }

        Genre genre = genre_opt.get();

        for (Title title : genre.getTitles()) {
            title.removeGenre(genre);
            genre.removeTitle(title);
        }

        genreRepository.deleteById(id);
        return true;
    }
}
