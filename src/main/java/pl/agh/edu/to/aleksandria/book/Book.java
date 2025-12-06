package pl.agh.edu.to.aleksandria.book;

import jakarta.persistence.*;
import pl.agh.edu.to.aleksandria.genre.Genre;
import pl.agh.edu.to.aleksandria.title.Title;

import java.util.List;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @ManyToOne
    private Title title;
    private boolean available;


    public Book() {}
    public Book(Title title, boolean available) {
        this.title = title;
        this.available = available;
    }

    @Override
    public String toString() {
        return title.toString();
    }
}
