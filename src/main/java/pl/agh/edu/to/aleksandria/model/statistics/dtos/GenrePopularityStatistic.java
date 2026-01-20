package pl.agh.edu.to.aleksandria.model.statistics.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GenrePopularityStatistic {
    private int genreId;
    private String genreName;
    private Integer rentedCount;
    private Integer rentedRightNow;
}
