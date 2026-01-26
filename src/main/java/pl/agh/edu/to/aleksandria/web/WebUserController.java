package pl.agh.edu.to.aleksandria.web;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.agh.edu.to.aleksandria.model.queue.QueueEntry;
import pl.agh.edu.to.aleksandria.model.queue.QueueService;
import pl.agh.edu.to.aleksandria.model.rental.Rental;
import pl.agh.edu.to.aleksandria.model.rental.RentalService;
import pl.agh.edu.to.aleksandria.model.user.User;
import pl.agh.edu.to.aleksandria.model.user.UserService;
import pl.agh.edu.to.aleksandria.model.user.dtos.CreateUserRequest;
import pl.agh.edu.to.aleksandria.model.user.dtos.UpdateUserRequest;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class WebUserController {

    private final UserService userService;
    private final RentalService rentalService;
    private final QueueService queueService;

    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "user-list";
    }

    @PostMapping("/web/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String createUser(@RequestParam String firstName,
                             @RequestParam String lastName,
                             @RequestParam String email,
                             @RequestParam String password,
                             @RequestParam String address,
                             @RequestParam String roleName,
                             RedirectAttributes redirectAttributes) {

        CreateUserRequest request = new CreateUserRequest(
                firstName, lastName, email, password, address, roleName
        );

        Optional<User> result = userService.createUser(request);

        if (result.isPresent()) {
            redirectAttributes.addFlashAttribute("successMessage", "User created successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: User with this email already exists.");
        }

        return "redirect:/users/list";
    }

    @GetMapping("/details/{id}")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public String userDetails(@PathVariable Integer id, Model model) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        List<Rental> rentals = rentalService.getRentalsByUser(id);

        model.addAttribute("user", user);
        model.addAttribute("rentals", rentals);
        List<QueueEntry> queueEntries = queueService.getAllUserQueueEntries(id);
        model.addAttribute("queueEntries", queueEntries);

        return "user-details";
    }

    @PostMapping("/web/update")
    @PreAuthorize("isAuthenticated()")
    public String updateUserProfile(@RequestParam Integer id,
                                    @RequestParam String firstName,
                                    @RequestParam String lastName,
                                    @RequestParam String email,
                                    @RequestParam String address,
                                    Authentication authentication) {

        User currentUser = (User) authentication.getPrincipal();
        if (!currentUser.getId().equals(id)) {
            throw new IllegalStateException("You can only edit your own profile.");
        }

        UpdateUserRequest request = new UpdateUserRequest(id, firstName, lastName, email, address);
        userService.updateUser(request);

        currentUser.setFirstName(firstName);
        currentUser.setLastName(lastName);
        currentUser.setEmail(email);
        currentUser.setAddress(address);

        return "redirect:/profile";
    }

    @PostMapping("/delete")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public String deleteUser(@RequestParam Integer id) {
        userService.deleteUser(id);
        return "redirect:/users/list";
    }
}
