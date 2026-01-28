package pl.agh.edu.to.aleksandria.model.statistics.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BookCountStatistic {
    private int titleId;
    private String bookTitle;
    private String bookAuthor;
    private Integer allBookCount;
    private Integer availableBookCount;
}
