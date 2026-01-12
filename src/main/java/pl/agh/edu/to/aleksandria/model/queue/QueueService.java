package pl.agh.edu.to.aleksandria.model.queue;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.agh.edu.to.aleksandria.model.queue.dtos.QueueRequest;
import pl.agh.edu.to.aleksandria.model.title.Title;
import pl.agh.edu.to.aleksandria.model.title.TitleService;
import pl.agh.edu.to.aleksandria.model.user.User;
import pl.agh.edu.to.aleksandria.model.user.UserService;
import pl.agh.edu.to.aleksandria.notifications.MailService;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class QueueService {

    private final MailService mailService;
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

    public List<QueueEntry> getAllUserQueueEntries(long userId) {
        return queueRepository.findAllByUser_Id(userId);
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

    @Transactional
    public Optional<QueueEntry> addUserToQueue(QueueRequest queueRequest) {
        int userId = queueRequest.getUserId();
        int titleId = queueRequest.getTitleId();
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

        mailService.sendOnQueueJoinedEmail(entry);

        return Optional.of(queueRepository.save(entry));
    }

    public int getPositionInQueue(int userId, int titleId) {
        // returns -1 if user or title not found or user not in queue
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
                return i + 1;
            }
        }
        return -1;
    }

    @Transactional
    public boolean removeUserFromQueue(QueueRequest request) {
        int userId = request.getUserId();
        int titleId = request.getTitleId();
        Optional<User> userOpt = userService.getUserById(userId);
        if (userOpt.isEmpty()) {
            return false;
        }
        Optional<Title> titleOpt = titleService.getTitleById(titleId);
        if (titleOpt.isEmpty()) {
            return false;
        }

        List<QueueEntry> allEntries = queueRepository.findAllByTitle(titleOpt.get());
        for (QueueEntry entry : allEntries) {
            if (entry.getUser().getId().equals(userOpt.get().getId())) {
                queueRepository.delete(entry);
                mailService.sendOnQueueLeftEmail(entry);
                return true;
            }
        }
        return false;
    }
}
