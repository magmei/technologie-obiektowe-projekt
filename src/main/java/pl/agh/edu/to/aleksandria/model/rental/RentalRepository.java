package pl.agh.edu.to.aleksandria.model.rental;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.agh.edu.to.aleksandria.model.book.Book;
import pl.agh.edu.to.aleksandria.model.title.Title;

import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Integer> {

    List<Rental> findByUser_Id(long user_id);

    List<Rental> findByBook_ItemId(long bookId);

    List<Rental> findByBook_Title_Id(long titleId);
}
