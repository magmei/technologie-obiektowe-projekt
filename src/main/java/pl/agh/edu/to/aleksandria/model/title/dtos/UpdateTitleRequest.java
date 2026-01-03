package pl.agh.edu.to.aleksandria.model.title.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.agh.edu.to.aleksandria.model.genre.Genre;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UpdateTitleRequest {
    private Integer id;
    private String titleName;
    private String author;
    private List<Genre> genresToAdd;
    private List<Genre> genresToRemove;
}
