package pl.agh.edu.to.aleksandria.model.statistics.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GenreCountStatistic {
    private int genreId;
    private String genreName;
    private Integer titleCount;
    private Integer bookCount;
}
