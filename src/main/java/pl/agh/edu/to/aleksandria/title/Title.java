package pl.agh.edu.to.aleksandria.title;

import jakarta.persistence.*;
import pl.agh.edu.to.aleksandria.genre.Genre;

import java.util.List;

@Entity
public class Title {

    @Id
    @GeneratedValue
    private int id;

    private String title;
    private String author;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "Title_Genres",
            joinColumns = @JoinColumn(name = "TITLE_ID"),
            inverseJoinColumns = @JoinColumn(name = "GENRE_ID")
    )
    private List<Genre> genres;

    public Title() {}
    public Title(String title, String author, List<Genre> genres) {
        this.title = title;
        this.author = author;
        this.genres = genres;
    }

    @Override
    public String toString() {
        return title + ", " + author + ", " + genres;
    }
}
