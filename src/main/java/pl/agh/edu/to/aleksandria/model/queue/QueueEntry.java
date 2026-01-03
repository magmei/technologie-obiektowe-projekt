package pl.agh.edu.to.aleksandria.model.queue;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.agh.edu.to.aleksandria.model.book.Book;
import pl.agh.edu.to.aleksandria.model.user.User;

@Entity
@NoArgsConstructor
@Table(name="queue")
public class QueueEntry {

    @Id
    @Setter
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TITLE_ID")
    private Book book;

    public QueueEntry(User user, Book book) {
        this.user = user;
        this.book = book;
    }
}
