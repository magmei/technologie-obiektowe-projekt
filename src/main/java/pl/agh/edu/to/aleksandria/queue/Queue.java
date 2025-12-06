package pl.agh.edu.to.aleksandria.queue;

import org.springframework.stereotype.Component;
import pl.agh.edu.to.aleksandria.book.Book;
import pl.agh.edu.to.aleksandria.user.User;

import java.util.List;

@Component
public class Queue {

    QueueRepository queueRepository;

    public Queue(QueueRepository queueRepository) {
        this.queueRepository = queueRepository;
    }

    public List<User> getUsersWaitingForBook(Book book){
        return null;
    }

    public void addUserToQueue(User user, Book book){}
}
