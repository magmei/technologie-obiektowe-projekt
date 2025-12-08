package pl.agh.edu.to.aleksandria.model.rental;

import jakarta.persistence.*;
import pl.agh.edu.to.aleksandria.model.book.Book;
import pl.agh.edu.to.aleksandria.model.user.User;

import java.time.LocalDate;

@Entity
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Book book;
    private LocalDate rentedOn;
    private LocalDate due;

    public Rental() {}

    public Rental(User user, Book book, LocalDate rentedOn, LocalDate due) {
        this.user = user;
        this.book = book;
        this.rentedOn = rentedOn;
        this.due = due;
    }
}
