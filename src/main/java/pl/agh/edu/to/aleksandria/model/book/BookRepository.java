package pl.agh.edu.to.aleksandria.model.book;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.agh.edu.to.aleksandria.model.title.Title;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

    List<Book> findByAvailable(boolean availability);

    List<Book> findByTitle(Title title);

    List<Book> findByAvailableTrueAndTitle_TitleName(String title_name);
}
