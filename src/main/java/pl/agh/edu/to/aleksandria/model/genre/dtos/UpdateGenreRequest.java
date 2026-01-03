package pl.agh.edu.to.aleksandria.model.genre.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.agh.edu.to.aleksandria.model.title.Title;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UpdateGenreRequest {
    private Integer genreId;
    private String genreName;
    private List<Title> titlesToAdd;
    private List<Title> titlesToDelete;
}
