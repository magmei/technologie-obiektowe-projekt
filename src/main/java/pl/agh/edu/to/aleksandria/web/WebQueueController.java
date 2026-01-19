package pl.agh.edu.to.aleksandria.web;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.agh.edu.to.aleksandria.model.queue.QueueService;
import pl.agh.edu.to.aleksandria.model.queue.dtos.QueueRequest;
import pl.agh.edu.to.aleksandria.model.user.User;

@Controller
@RequestMapping("/queue")
@RequiredArgsConstructor
public class WebQueueController {

    private final QueueService queueService;

    @PostMapping("/web/join")
    @PreAuthorize("hasRole('READER')")
    public String joinQueue(@RequestParam Integer titleId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        QueueRequest request = new QueueRequest(user.getId(), titleId);
        queueService.addUserToQueue(request);

        return "redirect:/titles/view/" + titleId;
    }

    @PostMapping("/web/admin/remove")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public String adminRemoveUserFromQueue(@RequestParam Integer userId,
                                           @RequestParam Integer titleId) {
        QueueRequest request = new QueueRequest(userId, titleId);
        queueService.removeUserFromQueue(request);

        return "redirect:/users/details/" + userId;
    }

    @PostMapping("/web/leave")
    @PreAuthorize("hasRole('READER')")
    public String leaveQueue(@RequestParam Integer titleId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        QueueRequest request = new QueueRequest(user.getId(), titleId);
        queueService.removeUserFromQueue(request);

        return "redirect:/titles/view/" + titleId;
    }
}
