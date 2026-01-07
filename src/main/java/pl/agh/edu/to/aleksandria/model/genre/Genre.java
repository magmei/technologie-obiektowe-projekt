package pl.agh.edu.to.aleksandria.model.genre;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.agh.edu.to.aleksandria.model.title.Title;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Genre {

    @Id
    @GeneratedValue
    private int id;

    @Setter
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "genres")
    private List<Title> titles;

    public Genre(String name) {
        this.name = name;
    }

    public void addTitle(Title title) {
        titles.add(title);
    }

    public void removeTitle(Title title) {
        titles.remove(title);
    }

    @Override
    public String toString() {
        return name;
    }

}
