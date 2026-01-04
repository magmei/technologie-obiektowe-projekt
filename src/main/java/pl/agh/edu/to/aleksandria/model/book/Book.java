package pl.agh.edu.to.aleksandria.model.book;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.agh.edu.to.aleksandria.model.title.Title;

@Entity
@NoArgsConstructor
public class Book {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer itemId;

    @ManyToOne
    private Title title;

    @Setter
    @Getter
    private boolean available;

    public Book(Title title, boolean available) {
        this.title = title;
        this.available = available;
    }

    @Override
    public String toString() {
        return title.toString();
    }
}
