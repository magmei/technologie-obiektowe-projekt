package pl.agh.edu.to.aleksandria.model.genre.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UpdateGenreRequest {
    private Integer genreId;
    private String genreName;
    private List<Integer> titlesToAdd;
    private List<Integer> titlesToDelete;
}
