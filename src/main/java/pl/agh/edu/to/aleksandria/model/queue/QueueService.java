package pl.agh.edu.to.aleksandria.model.queue;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pl.agh.edu.to.aleksandria.model.book.Book;
import pl.agh.edu.to.aleksandria.model.book.BookService;
import pl.agh.edu.to.aleksandria.model.queue.dtos.AddToQueueRequest;
import pl.agh.edu.to.aleksandria.model.title.Title;
import pl.agh.edu.to.aleksandria.model.title.TitleService;
import pl.agh.edu.to.aleksandria.model.user.User;
import pl.agh.edu.to.aleksandria.model.user.UserService;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class QueueService {

    QueueRepository queueRepository;
    UserService userService;
    TitleService titleService;

    @PostConstruct
    public void onServiceStarted() {
        System.out.println("Queue service started");
    }

    @PreDestroy
    public void onServiceStopped() {
        System.out.println("Queue service destroyed");
    }

    public List<User> getUsersWaitingForTitle(int titleId) {
        Optional<Title> titleOpt = titleService.getTitleById(titleId);
        if (titleOpt.isEmpty()) {
            return List.of();
        }
        List<QueueEntry> entries = queueRepository.findAllByTitle(titleOpt.get());
        entries.sort(Comparator.comparing(QueueEntry::getRequestDate));
        return entries.stream().map(QueueEntry::getUser).toList();
    }

    public Optional<QueueEntry> addUserToQueue(AddToQueueRequest addToQueueRequest) {
        int userId = addToQueueRequest.getUserId();
        int titleId = addToQueueRequest.getTitleId();
        Optional<User> userOpt = userService.getUserById(userId);
        if (userOpt.isEmpty()) {
            return Optional.empty();
        }
        Optional<Title> titleOpt = titleService.getTitleById(titleId);
        if (titleOpt.isEmpty()) {
            return Optional.empty();
        }

        // no duplicate entries
        if (queueRepository.existsByUserAndTitle(userOpt.get(), titleOpt.get())) {
            return Optional.empty();
        }

        QueueEntry entry = new QueueEntry(userOpt.get(), titleOpt.get(), LocalDateTime.now());

        return Optional.of(queueRepository.save(entry));
    }

    public int getPositionInQueue(int userId, int titleId) {
        Optional<User> userOpt = userService.getUserById(userId);
        if (userOpt.isEmpty()) {
            return -1;
        }
        Optional<Title> titleOpt = titleService.getTitleById(titleId);
        if (titleOpt.isEmpty()) {
            return -1;
        }

        List<QueueEntry> allEntries = queueRepository.findAllByTitle(titleOpt.get());
        if (allEntries.isEmpty()) {
            return -1;
        }
        allEntries.sort(Comparator.comparing(QueueEntry::getRequestDate));
        for (int i = 0; i < allEntries.size(); i++) {
            if (allEntries.get(i).getUser().getId().equals(userOpt.get().getId())) {
                return i + 1; // positions are 1-based
            }
        }
        return -1;
    }

    public void removeUserFromQueue(int userId, int titleId) {
        Optional<User> userOpt = userService.getUserById(userId);
        if (userOpt.isEmpty()) {
            return;
        }
        Optional<Title> titleOpt = titleService.getTitleById(titleId);
        if (titleOpt.isEmpty()) {
            return;
        }

        List<QueueEntry> allEntries = queueRepository.findAllByTitle(titleOpt.get());
        for (QueueEntry entry : allEntries) {
            if (entry.getUser().getId().equals(userOpt.get().getId())) {
                queueRepository.delete(entry);
                return;
            }
        }
    }
}
