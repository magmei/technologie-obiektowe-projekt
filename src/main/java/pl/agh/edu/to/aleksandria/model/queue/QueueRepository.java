package pl.agh.edu.to.aleksandria.model.queue;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.agh.edu.to.aleksandria.model.book.Book;
import pl.agh.edu.to.aleksandria.model.title.Title;
import pl.agh.edu.to.aleksandria.model.user.User;

import java.util.List;

public interface QueueRepository extends JpaRepository<QueueEntry, Integer> {
    boolean existsByUserAndTitle(User user, Title title);

    List<QueueEntry> findAllByTitle(Title title);
}
