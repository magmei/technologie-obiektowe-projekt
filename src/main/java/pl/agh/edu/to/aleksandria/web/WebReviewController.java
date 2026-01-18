package pl.agh.edu.to.aleksandria.web;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.agh.edu.to.aleksandria.model.review.ReviewService;
import pl.agh.edu.to.aleksandria.model.review.dtos.CreateReviewRequest;
import pl.agh.edu.to.aleksandria.model.user.User;

@Controller
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class WebReviewController {

    private final ReviewService reviewService;

    // POST /reviews/web/add
    @PostMapping("/web/add")
    @PreAuthorize("hasRole('READER')")
    public String addReview(@RequestParam Integer titleId,
                            @RequestParam Integer rating,
                            @RequestParam String comment) {

        // Get currently logged-in user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        // Create the DTO
        CreateReviewRequest request = new CreateReviewRequest(titleId, user.getId(), comment, rating);

        reviewService.createReview(request);

        return "redirect:/titles/view/" + titleId;
    }

    // POST /reviews/web/delete
    @PostMapping("/web/delete")
    @PreAuthorize("isAuthenticated()")
    public String deleteReview(@RequestParam Integer reviewId,
                               @RequestParam Integer titleId) {
        // The service or security layer should ideally handle the "isOwner" check.
        // For simplicity in this web layer, we rely on the service method or pre-checks.
        // However, standard @PreAuthorize on the service method is best.
        // Here we just call delete.

        reviewService.deleteReview(reviewId);

        return "redirect:/titles/view/" + titleId;
    }
}
