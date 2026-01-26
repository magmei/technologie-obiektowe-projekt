package pl.agh.edu.to.aleksandria.web;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.agh.edu.to.aleksandria.model.queue.QueueEntry;
import pl.agh.edu.to.aleksandria.model.queue.QueueService;
import pl.agh.edu.to.aleksandria.model.rental.Rental;
import pl.agh.edu.to.aleksandria.model.rental.RentalService;
import pl.agh.edu.to.aleksandria.model.user.User;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final RentalService rentalService;
    private final QueueService queueService;

    @GetMapping("/profile")
    public String profile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        model.addAttribute("user", user);

        List<Rental> rentals = rentalService.getRentalsByUser(user.getId());
        model.addAttribute("rentals", rentals);

        List<QueueEntry> queue = queueService.getAllUserQueueEntries(user.getId());
        model.addAttribute("queue", queue);

        return "profile";
    }
}
