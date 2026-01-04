package pl.agh.edu.to.aleksandria.model.rental;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.agh.edu.to.aleksandria.model.book.Book;
import pl.agh.edu.to.aleksandria.model.title.Title;

import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Integer> {
    List<Rental> findByBook(Book book);

    Rental findByUser_Id(Long user_id);
}
