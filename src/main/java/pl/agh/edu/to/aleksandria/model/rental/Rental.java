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
    @Getter
    private Book book;

    @Getter
    @Setter
    private LocalDate rentedOn;

    @Getter
    @Setter
    private LocalDate due;

    @Getter
    @Setter
    private LocalDate returnedOn;

    public Rental(User user, Book book, LocalDate rentedOn, LocalDate due, LocalDate returnedOn) {
        this.user = user;
        this.book = book;
        this.rentedOn = rentedOn;
        this.due = due;
        this.returnedOn = returnedOn;
    }

    @Override
    public String toString() {
        String userRep = (user == null) ? "null"
                : user.getClass().getSimpleName() + "@" + Integer.toHexString(System.identityHashCode(user));
        String bookRep = (book == null) ? "null"
                : book.getClass().getSimpleName() + "@" + Integer.toHexString(System.identityHashCode(book));

        return "Rental{" +
                "id=" + id +
                ", user=" + userRep +
                ", book=" + bookRep +
                ", rentedOn=" + rentedOn +
                ", due=" + due +
                ", returnedOn=" + returnedOn +
                '}';
    }
}

