package pl.agh.edu.to.aleksandria.model.title;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
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

        Title title = new Title(request.getTitleName(), request.getAuthor(), request.getGenres());
        return Optional.of(titleRepository.save(title));
    }

    public Optional<Title> updateTitle(UpdateTitleRequest request) {
        Optional<Title> title = getTitleById(request.getId());

        if (title.isEmpty()) {
            return Optional.empty();
        }

        Title updated_title = title.get();

        if (!Objects.equals(updated_title.getTitleName(), request.getTitleName())) {
            updated_title.setTitleName(request.getTitleName());
        }

        if (!Objects.equals(updated_title.getAuthor(), request.getAuthor())) {
            updated_title.setAuthor(request.getAuthor());
        }

        for (Genre genre : request.getGenresToAdd()) {
            updated_title.addGenre(genre);
        }

        for (Genre genre : request.getGenresToRemove()) {
            updated_title.removeGenre(genre);
        }

        return Optional.of(titleRepository.save(updated_title));
    }

    public boolean deleteTitle(Integer id) {
        Optional<Title> title = getTitleById(id);

        if (title.isEmpty()) {
            return false;
        }

        titleRepository.deleteById(id);
        return true;
    }

}
