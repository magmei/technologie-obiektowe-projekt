package pl.agh.edu.to.aleksandria.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.agh.edu.to.aleksandria.model.user.User;
import pl.agh.edu.to.aleksandria.model.user.UserService;

@Component("roleSecurity")
@RequiredArgsConstructor
public class RoleSecurity {
    private final UserService userService;


    public boolean canModify(Integer targetId) {
        // librarians can modify readers, admins can modify anyone
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return true;
        }

        boolean isLibrarian = auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_LIBRARIAN"));

        if (!isLibrarian) {
            return false;
        }

        User targetUser = userService.getUserById(targetId).orElse(null);
        if (targetUser == null) {
            return false;
        }

        return targetUser.getRole().getName().equalsIgnoreCase("READER");
    }
}
