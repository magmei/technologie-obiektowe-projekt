package pl.agh.edu.to.aleksandria.web;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.agh.edu.to.aleksandria.model.review.Review;
import pl.agh.edu.to.aleksandria.model.review.ReviewService;
import pl.agh.edu.to.aleksandria.model.review.dtos.CreateReviewRequest;
import pl.agh.edu.to.aleksandria.model.review.dtos.UpdateReviewRequest;
import pl.agh.edu.to.aleksandria.model.user.User;

import java.util.Optional;

@Controller
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class WebReviewController {

    private final ReviewService reviewService;

    @PostMapping("/web/add")
    @PreAuthorize("hasRole('READER')")
    public String addReview(@RequestParam Integer titleId, @RequestParam Integer rating, @RequestParam String comment) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        CreateReviewRequest request = new CreateReviewRequest(titleId, user.getId(), comment, rating);
        reviewService.createReview(request);

        return "redirect:/titles/view/" + titleId;
    }

    @PostMapping("/web/update")
    @PreAuthorize("isAuthenticated()")
    public String updateReview(@RequestParam Integer reviewId, @RequestParam Integer titleId, @RequestParam Integer rating, @RequestParam String comment) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();

        Optional<Review> existingReview = reviewService.getReviewById(reviewId);

        if (existingReview.isPresent()) {
            Review review = existingReview.get();

            if (review.getUserId() == currentUser.getId()) {
                UpdateReviewRequest request = new UpdateReviewRequest(reviewId, comment, rating);
                reviewService.updateReview(request);
            } else {
                System.out.println("Unauthorized attempt to edit review by user " + currentUser.getId());
            }
        }

        return "redirect:/titles/view/" + titleId;
    }

    @PostMapping("/web/delete")
    @PreAuthorize("isAuthenticated()")
    public String deleteReview(@RequestParam Integer reviewId, @RequestParam Integer titleId) {

        reviewService.deleteReview(reviewId);

        return "redirect:/titles/view/" + titleId;
    }
}
