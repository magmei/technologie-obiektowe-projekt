package pl.agh.edu.to.aleksandria.model.title;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.agh.edu.to.aleksandria.model.genre.Genre;

import java.util.List;

@Entity
@NoArgsConstructor
public class Title {

    @Id
    @Getter
    @GeneratedValue
    private int id;

    @Getter
    @Setter
    private String titleName;

    @Getter
    @Setter
    private String author;

    @Getter
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "Title_Genres",
            joinColumns = @JoinColumn(name = "TITLE_ID"),
            inverseJoinColumns = @JoinColumn(name = "GENRE_ID")
    )
    private List<Genre> genres;

    public Title(String title, String author, List<Genre> genres) {
        this.titleName = title;
        this.author = author;
        this.genres = genres;
    }

    public void addGenre(Genre genre) {
        this.genres.add(genre);
    }

    public void removeGenre(Genre genre) {
        this.genres.remove(genre);
    }

    @Override
    public String toString() {
        return titleName + ", " + author + ", " + genres;
    }
}
