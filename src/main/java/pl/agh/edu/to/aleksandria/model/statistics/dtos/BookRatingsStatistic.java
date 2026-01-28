package pl.agh.edu.to.aleksandria.model.statistics.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class BookRatingsStatistic {
    private int titleId;
    private String titleName;
    private String author;
    private Double averageRating;
    private Integer ratingCount;
}
