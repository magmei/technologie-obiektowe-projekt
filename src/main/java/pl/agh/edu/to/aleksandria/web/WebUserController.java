package pl.agh.edu.to.aleksandria.web;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.agh.edu.to.aleksandria.model.user.User;
import pl.agh.edu.to.aleksandria.model.user.UserService;
import pl.agh.edu.to.aleksandria.model.user.dtos.CreateUserRequest;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class WebUserController {

    private final UserService userService;

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
                             RedirectAttributes redirectAttributes) { // <--- Inject this

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

    @PostMapping("/delete")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public String deleteUser(@RequestParam Integer id) {
        // The service layer (or RoleSecurity) handles the logic of
        // "Librarian cannot delete Admin".
        userService.deleteUser(id);
        return "redirect:/users/list";
    }
}
