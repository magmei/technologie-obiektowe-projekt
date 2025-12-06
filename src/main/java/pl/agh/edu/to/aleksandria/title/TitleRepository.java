package pl.agh.edu.to.aleksandria.title;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.agh.edu.to.aleksandria.queue.QueueEntry;

public interface TitleRepository extends JpaRepository<Title, Integer> {
}
