package pl.agh.edu.to.aleksandria.model.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.agh.edu.to.aleksandria.model.user.dtos.CreateUserRequest;
import pl.agh.edu.to.aleksandria.model.user.dtos.UpdateUserRequest;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET /users/all
    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // GET /users/search/id
    @GetMapping("/search/by_id")
    public ResponseEntity<Object> getUserById(@RequestParam Integer id) {
        return userService.getUserById(id)
                .<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "error", "No user with this ID found"
                        )));
    }

    // GET /users/search/by_role/role
    @GetMapping("/search/by_role")
    public List<User> getUsersByRole(@RequestParam String role) {
        return userService.getUsersByRole(role);
    }

    // GET /users/search/by_email
    @GetMapping("/search/by_email")
    public ResponseEntity<Object> getUsersByEmail(@RequestParam String email) {
        return userService.getUserByEmail(email)
                .<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "error", "No user with this email found"
                        )));
    }

    // GET /users/search/by_fullname/
    @GetMapping("/search/by_fullname")
    public List<User> getUsersByFullName(@RequestParam String firstName, @RequestParam String lastName) {
        return userService.getUsersByFullName(firstName, lastName);
    }

    // POST /users/create
    @PostMapping("create")
    public ResponseEntity<Object> createUser(@RequestBody CreateUserRequest request) {
        return userService.createUser(request)
                .<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of(
                                "error", "Failed to create user"
                        )));
    }

    // PUT /users/update
    @PutMapping("update")
    public ResponseEntity<Object> updateUser(@RequestBody UpdateUserRequest request) {
        return userService.updateUser(request)
                .<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of(
                                "error", "Failed to update user"
                        )));
    }

    // DELETE /users/delete
    @DeleteMapping("delete")
    public ResponseEntity<Object> deleteUser(@RequestParam Integer id) {
        boolean deleted = userService.deleteUser(id);

        if (deleted) {
            return ResponseEntity.ok(Map.of("message", "User deleted"));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "User not found"));
    }
}
