package pl.agh.edu.to.aleksandria.model.title;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TitleRepository extends JpaRepository<Title, Integer> {

    Optional<Title> findByTitleName(String name);

    List<Title> findByAuthor(String author);

}
