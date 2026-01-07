package pl.agh.edu.to.aleksandria.model.title;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.agh.edu.to.aleksandria.model.genre.Genre;
import pl.agh.edu.to.aleksandria.model.genre.GenreRepository;
import pl.agh.edu.to.aleksandria.model.title.dtos.CreateTitleRequest;
import pl.agh.edu.to.aleksandria.model.title.dtos.UpdateTitleRequest;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TitleService {

    private final TitleRepository titleRepository;
    private final GenreRepository genreRepository;

    @PostConstruct
    public void onServiceStarted() {
        System.out.println("Title service started");
    }

    @PreDestroy
    public void onServiceStopped() {
        System.out.println("Title service stopped");
    }

    public List<Title> getAllTitles() {
        return titleRepository.findAll();
    }

    public Optional<Title> getTitleById(Integer id) {
        return titleRepository.findById(id);
    }

    public Optional<Title> getTitleByName(String name) {
        return titleRepository.findByTitleName(name);
    }

    public List<Title> getTitleByAuthor(String author) {
        return titleRepository.findByAuthor(author);
    }

    public List<Title> getTitleByGenre(String genre) {
        Optional<Genre> genre_opt = genreRepository.findByName(genre);

        if (genre_opt.isEmpty()) {
            return List.of();
        }

        return genre_opt.get().getTitles();
    }

    public Optional<Title> createTitle(CreateTitleRequest request) {
        Optional<Title> title_opt = getTitleByName(request.getTitleName());

        if (title_opt.isPresent()) {
            return Optional.empty();
        }

        Title title = new Title(
                request.getTitleName(),
                request.getAuthor(),
                genreRepository.findAllById(request.getGenres())
        );

        return Optional.of(titleRepository.save(title));
    }

    @Transactional
    public Optional<Title> updateTitle(UpdateTitleRequest request) {
        Optional<Title> title = getTitleById(request.getId());

        if (title.isEmpty()) {
            return Optional.empty();
        }

        Title updated_title = title.get();

        List<Genre> genresToAdd = genreRepository.findAllById(request.getGenresToAdd());
        List<Genre> genresToRemove = genreRepository.findAllById(request.getGenresToDelete());

        if (
                genresToAdd.size() < request.getGenresToAdd().size() ||
                genresToRemove.size() < request.getGenresToAdd().size()
        ) {
            return Optional.empty();
        }

        updated_title.setTitleName(request.getTitleName());
        updated_title.setAuthor(request.getAuthor());

        for (Genre genre : genresToAdd) {
            updated_title.addGenre(genre);
            genre.addTitle(updated_title);
        }

        for (Genre genre : genresToRemove) {
            updated_title.removeGenre(genre);
            genre.removeTitle(updated_title);
        }

        return Optional.of(updated_title);
    }

    @Transactional
    public boolean deleteTitle(Integer id) {
        Optional<Title> title_opt = getTitleById(id);

        if (title_opt.isEmpty()) {
            return false;
        }

        Title title = title_opt.get();

        for (Genre genre : title.getGenres()) {
            genre.removeTitle(title);
            title.removeGenre(genre);
        }

        titleRepository.deleteById(id);
        return true;
    }

}
