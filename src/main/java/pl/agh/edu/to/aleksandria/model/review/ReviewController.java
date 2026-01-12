package pl.agh.edu.to.aleksandria.model.review;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.agh.edu.to.aleksandria.model.review.dtos.CreateReviewRequest;
import pl.agh.edu.to.aleksandria.model.review.dtos.UpdateReviewRequest;
import pl.agh.edu.to.aleksandria.model.title.Title;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // GET /reviews/byTitle?titleId=
    @GetMapping("/byTitle")
    @PreAuthorize("hasAnyRole('READER', 'LIBRARIAN', 'ADMIN')")
    public List<Review> getAllReviewsByTitleId(@RequestParam int titleId) {return reviewService.getAllReviewsByTitleId(titleId);}

    // POST /reviews/create
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('READER')")
    public ResponseEntity<Object> createReview(@RequestBody CreateReviewRequest request) {
        return this.optionalToResponseEntity(
                reviewService.createReview(request),
                HttpStatus.BAD_REQUEST,
                "Could not create review"
        );
    }

    @PutMapping("/update/{reviewId}")
    @PreAuthorize("isAuthenticated() and (hasAnyRole('ADMIN') or @reviewSecurity.isOwner(#reviewId, authentication.principal.id))")
    public ResponseEntity<Object> updateReview(@PathVariable int reviewId, @RequestBody UpdateReviewRequest request) {
        request.setReviewId(reviewId);
        return this.optionalToResponseEntity(
                reviewService.updateReview(request),
                HttpStatus.BAD_REQUEST,
                "Failed to update the review"
        );
    }


    // DELETE /reviews/delete?reviewId=
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public boolean deleteReview(@RequestParam Integer reviewId) {return reviewService.deleteReview(reviewId);}


    private ResponseEntity<Object> optionalToResponseEntity(Optional<Review> review, HttpStatus status, String error) {
        return review.<ResponseEntity<Object>>map(ResponseEntity::ok).orElse(
                ResponseEntity.status(status).body(Map.of("error", error)));
    }
}
