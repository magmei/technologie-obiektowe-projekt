package pl.agh.edu.to.aleksandria.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.agh.edu.to.aleksandria.model.review.ReviewRepository;

@Component("reviewSecurity")
@RequiredArgsConstructor
public class ReviewSecurity {
    private final ReviewRepository reviewRepository;

    public boolean isOwner(int reviewId, int userId) {
        return reviewRepository.findById(reviewId)
                .map(review -> review.getUserId() == userId)
                .orElse(false);
    }
}
