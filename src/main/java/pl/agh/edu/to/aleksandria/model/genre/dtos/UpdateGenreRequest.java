package pl.agh.edu.to.aleksandria.model.genre.dtos;

import lombok.Getter;
import lombok.Setter;
import pl.agh.edu.to.aleksandria.model.title.Title;

import java.util.List;

@Getter
@Setter
public class UpdateGenreRequest {
    private Integer genreId;
    private String genreName;
    private List<Title> titlesToAdd;
    private List<Title> titlesToDelete;

    public UpdateGenreRequest(Integer genreId, String genreName, List<Title> titlesToAdd, List<Title> titlesToDelete) {
        this.genreId = genreId;
        this.genreName = genreName;
        this.titlesToAdd = titlesToAdd;
        this.titlesToDelete = titlesToDelete;
    }
}
