package pl.agh.edu.to.aleksandria.model.queue;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.agh.edu.to.aleksandria.model.book.Book;
import pl.agh.edu.to.aleksandria.model.user.User;

import java.util.List;

@Component
@AllArgsConstructor
public class QueueService {

    QueueRepository queueRepository;

    public List<User> getUsersWaitingForBook(Book book){
        return null;
    }

    public void addUserToQueue(User user, Book book) {

    }

    public int getPositionInQueue(User user, Book book) {
        return -1;
    }
}
