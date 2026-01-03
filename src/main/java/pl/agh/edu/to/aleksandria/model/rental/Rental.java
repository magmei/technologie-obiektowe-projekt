package pl.agh.edu.to.aleksandria.model.rental;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.agh.edu.to.aleksandria.model.book.Book;
import pl.agh.edu.to.aleksandria.model.user.User;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
public class Rental {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Book book;

    @Getter
    @Setter
    private LocalDate rentedOn;

    @Getter
    @Setter
    private LocalDate due;

    public Rental(User user, Book book, LocalDate rentedOn, LocalDate due) {
        this.user = user;
        this.book = book;
        this.rentedOn = rentedOn;
        this.due = due;
    }
}
