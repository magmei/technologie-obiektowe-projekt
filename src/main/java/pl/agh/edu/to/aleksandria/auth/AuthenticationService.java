package pl.agh.edu.to.aleksandria.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.agh.edu.to.aleksandria.auth.dtos.AuthenticationRequest;
import pl.agh.edu.to.aleksandria.auth.dtos.AuthenticationResponse;
import pl.agh.edu.to.aleksandria.auth.dtos.RegisterRequest;
import pl.agh.edu.to.aleksandria.model.user.UserService;
import pl.agh.edu.to.aleksandria.model.user.dtos.CreateUserRequest;
import pl.agh.edu.to.aleksandria.security.JwtService;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = userService.createUser(
                new CreateUserRequest(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        passwordEncoder.encode(request.getPassword()),
                        "default address", // temporary hardcoded address
                        "reader"
                )
        );

        var jwtToken = jwtService.generateToken(user.orElseThrow());

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userService.loadUserByUsername(request.getEmail());
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
