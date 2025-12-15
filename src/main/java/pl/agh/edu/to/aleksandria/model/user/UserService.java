package pl.agh.edu.to.aleksandria.model.user;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.agh.edu.to.aleksandria.model.role.Role;
import pl.agh.edu.to.aleksandria.model.role.RoleRepository;
import pl.agh.edu.to.aleksandria.model.user.dtos.CreateUserRequest;
import pl.agh.edu.to.aleksandria.model.user.dtos.UpdateUserRequest;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void onServiceStarted() {
        System.out.println("User Service started");
    }

    @PreDestroy
    public void onServiceDestroyed() {
        System.out.println("User Service destroyed");
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    public List<User> getUsersByRole(String role) {
        return userRepository.findByRole_Name(role);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User loadUserByUsername(String email) {
        return Objects.requireNonNull(this.getUserByEmail(email).orElse(null));
    }

    public List<User> getUsersByFullName(String firstName, String lastName) {
        return userRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    public Optional<User> createUser(CreateUserRequest request) {
        // If role does not exist, do not register new user
        Optional<Role> userRole = roleRepository.findByName(request.getRoleName());
        if (userRole.isEmpty()) {
            return Optional.empty();
        }

        // If user with this email already exist, do not register new user
        Optional<User> user = getUserByEmail(request.getEmail());
        if (user.isPresent()) {
            return Optional.empty();
        }

        // Register new one
        User newUser = new User(request.getFirstName(), request.getLastName(), request.getAddress(), request.getEmail(), passwordEncoder.encode(request.getPassword()), userRole.get());
        return Optional.of(userRepository.save(newUser));
    }

    public Optional<User> updateUser(UpdateUserRequest request) {
        // If user does not exist, do not update anything
        Optional<User> savedUser = userRepository.findById(request.getId());
        if (savedUser.isEmpty()) {
            return Optional.empty();
        }

        User user = savedUser.get();

        if (request.getFirstName() != null && !Objects.equals(request.getFirstName(), user.getFirstName())) {
            user.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null && !Objects.equals(request.getLastName(), user.getLastName())) {
            user.setLastName(request.getLastName());
        }

        if (request.getAddress() != null && !Objects.equals(request.getAddress(), user.getAddress())) {
            user.setAddress(request.getAddress());
        }

        if (request.getEmail() != null && !Objects.equals(request.getEmail(), user.getEmail())) {
            // If there is some user with email we want to set, abort
            Optional<User> userWithNewEmail = userRepository.findByEmail(request.getEmail());
            if (userWithNewEmail.isPresent()) {
                return Optional.empty();
            }

            user.setEmail(request.getEmail());
        }

        return Optional.of(userRepository.save(user));
    }

    public boolean deleteUser(Integer id) {
        Optional<User> userToDelete = userRepository.findById(id);
        if (userToDelete.isEmpty()) {
            return false;
        }

        userRepository.delete(userToDelete.get());
        return true;
    }
}
