package pl.agh.edu.to.aleksandria.model.queue;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.agh.edu.to.aleksandria.model.queue.dtos.QueueRequest;
import pl.agh.edu.to.aleksandria.model.user.User;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/queue")
public class QueueController {

    private final QueueService queueService;

    // GET /queue?title_id=
    @GetMapping("/queue")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public ResponseEntity<Object> getQueue(@RequestParam Integer title_id) {
        return this.optionalToResponseEntity(
                queueService.getUsersWaitingForTitle(title_id),
                HttpStatus.NOT_FOUND,
                "No queue for this title ID found"
        );
    }

    // GET /queue/position?title_id=&user_id=
    @GetMapping("/queue/position")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN') or #user_id == principal.id")
    public int getUserPositionInQueue(@RequestParam Integer title_id, @RequestParam Integer user_id) {
        return queueService.getPositionInQueue(user_id, title_id);
    }

    // POST /queue/join?title_id=&user_id=
    @PostMapping("/join")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN') or #user_id == principal.id")
    public ResponseEntity<Object> joinQueue(@RequestBody QueueRequest request) {
        return this.optionalToResponseEntity(
                queueService.addUserToQueue(request),
                HttpStatus.BAD_REQUEST,
                "Could not add user to the queue"
        );
    }

    // POST /queue/leave?title_id=&user_id=
    @PostMapping("/leave")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN') or #user_id == principal.id")
    public ResponseEntity<Object> leaveQueue(@RequestBody QueueRequest request) {
        if (queueService.removeUserFromQueue(request)) {
            return ResponseEntity.ok().build();
        } else {
            return new ResponseEntity<>("Could not remove user from the queue", HttpStatus.BAD_REQUEST);
        }
    }

    private ResponseEntity<Object> optionalToResponseEntity(List<User> usersWaitingForTitle, HttpStatus httpStatus, String s) {
        if (usersWaitingForTitle.isEmpty()) {
            return new ResponseEntity<>(s, httpStatus);
        } else {
            return new ResponseEntity<>(usersWaitingForTitle, HttpStatus.OK);
        }
    }

    private ResponseEntity<Object> optionalToResponseEntity(Optional<QueueEntry> queueEntry, HttpStatus httpStatus, String s) {
        return queueEntry.<ResponseEntity<Object>>map(entry -> new ResponseEntity<>(entry, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(s, httpStatus));
    }


}
