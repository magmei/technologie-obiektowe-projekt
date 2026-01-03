package pl.agh.edu.to.aleksandria.model.book;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.agh.edu.to.aleksandria.model.title.Title;
import pl.agh.edu.to.aleksandria.model.title.TitleRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final TitleRepository titleRepository;

    @PostConstruct
    public void onServiceStarted() {
        System.out.println("Book service started");
    }

    @PreDestroy
    public void onServiceDestroyed() {
        System.out.println("Book service destroyed");
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Integer id) {
        return bookRepository.findById(id);
    }

    public List<Book> getBooksByAvailability(boolean availability) {
        return bookRepository.findByAvailable(availability);
    }

    public List<Book> getAvailableBooksByTitleName(String title) {
        return bookRepository.findByAvailableTrueAndTitle_TitleName(title);
    }

    public Optional<Book> createBook(Integer title_id) {
        Optional<Title> title = titleRepository.findById(title_id);

        if (title.isEmpty()) {
            return Optional.empty();
        }

        Book book = new Book(title.get(), true);
        return Optional.of(bookRepository.save(book));
    }

    public Optional<Book> changeAvailability(Integer id, boolean availability) {
        Optional<Book> book = getBookById(id);

        if (book.isEmpty()) {
            return Optional.empty();
        }

        Book actual_book = book.get();

        actual_book.setAvailable(availability);
        return Optional.of(bookRepository.save(actual_book));
    }

    public boolean deleteBook(Integer id) {
        Optional<Book> book = getBookById(id);
        if (book.isEmpty()) {
            return false;
        }

        bookRepository.delete(book.get());
        return true;
    }
}
