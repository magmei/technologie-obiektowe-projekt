package pl.agh.edu.to.aleksandria.model.review;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    int reviewId;

    @Setter
    @Getter
    int titleId;

    @Setter
    @Getter
    int userId;

    @Setter
    @Getter
    String reviewText;

    @Setter
    @Getter
    int rating;

    public Review() {
    }

    public Review(int titleId, int userId, String reviewText, int rating) {
        this.titleId = titleId;
        this.userId = userId;
        this.reviewText = reviewText;
        this.rating = rating;
    }


}
