package pl.agh.edu.to.aleksandria.model.queue;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.agh.edu.to.aleksandria.model.book.Book;
import pl.agh.edu.to.aleksandria.model.user.User;

import java.time.LocalDate;
import java.util.List;

@Component
@AllArgsConstructor
public class Queue {

    QueueRepository queueRepository;

    public List<User> getUsersWaitingForBook(Book book){
        return null;
    }

    public void addUserToQueue(User user, Book book){}
}
