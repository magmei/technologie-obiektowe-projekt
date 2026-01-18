package pl.agh.edu.to.aleksandria.web;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.agh.edu.to.aleksandria.model.title.TitleService;

@Controller
@AllArgsConstructor
public class HomeController {
    private final TitleService titleService;

    @GetMapping("/")
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Check if a user is logged in (and not just an "anonymous" guest)
        boolean isLoggedIn = auth != null &&
                auth.isAuthenticated() &&
                !(auth instanceof AnonymousAuthenticationToken);

        if (isLoggedIn) {
            model.addAttribute("titles", titleService.getAllTitles());
        }

        return "home";
    }
}
