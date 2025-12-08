package pl.agh.edu.to.aleksandria.model.genre;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import pl.agh.edu.to.aleksandria.model.title.Title;

import java.util.List;

@Entity
public class Genre {

    @Id
    @GeneratedValue
    private int id;

    private String name;

    @ManyToMany(mappedBy = "genres")
    private List<Title> titles;

    public Genre() {}

    public Genre(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addTitle(Title title) {
        titles.add(title);
    }

    @Override
    public String toString() {
        return name;
    }

}
