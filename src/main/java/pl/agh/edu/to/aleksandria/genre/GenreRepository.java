package pl.agh.edu.to.aleksandria.genre;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.agh.edu.to.aleksandria.book.Book;

public interface GenreRepository extends JpaRepository<Genre, Integer> {
}
