package pl.agh.edu.to.aleksandria.model.review;

import org.springframework.stereotype.Service;
import pl.agh.edu.to.aleksandria.model.review.dtos.CreateReviewRequest;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> getAllReviewsByTitleId(int titleId) {return reviewRepository.findByTitleId(titleId);}

    public Optional<Review> getReviewById(int id) {return reviewRepository.findById(id);}

    public Optional<Review> createReview(CreateReviewRequest request) {
        Review review = new Review(request.getTitleId(), request.getUserId(), request.getReviewText(), request.getRating());

        return Optional.of(reviewRepository.save(review));
    }

    public boolean deleteReview(int reviewId) {
        Optional<Review> reviewToDelete = getReviewById(reviewId);
        if (reviewToDelete.isEmpty()) {
            return false;
        }

        reviewRepository.deleteById(reviewId);
        return true;
    }
}
