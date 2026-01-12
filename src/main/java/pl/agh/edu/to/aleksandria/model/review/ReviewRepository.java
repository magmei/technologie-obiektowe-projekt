package pl.agh.edu.to.aleksandria.model.review;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByTitleId(int titleId);
}
