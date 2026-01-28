package pl.agh.edu.to.aleksandria.model.review.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateReviewRequest {
    private int titleId;
    private int userId;
    private String reviewText;
    private int rating;
}
