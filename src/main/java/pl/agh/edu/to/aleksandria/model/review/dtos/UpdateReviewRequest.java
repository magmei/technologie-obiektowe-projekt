package pl.agh.edu.to.aleksandria.model.review.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateReviewRequest {
    private int reviewId;
    private String reviewText;
    private Integer rating;

    public UpdateReviewRequest(String reviewText, Integer rating) {
        this.reviewText = reviewText;
        this.rating = rating;
    }
}
