package pl.agh.edu.to.aleksandria.book;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    private int titleId;

    public Book() {}
    public Book(int titleId) {
        this.titleId = titleId;
    }
}
