package pl.agh.edu.to.aleksandria.model.queue;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QueueRepository extends JpaRepository<QueueEntry, Integer> {
}
