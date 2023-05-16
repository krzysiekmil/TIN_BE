package pjwstk.s20124.tin.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pjwstk.s20124.tin.configuration.security.jwt.TokenProvider;
import pjwstk.s20124.tin.model.ERole;
import pjwstk.s20124.tin.model.Role;
import pjwstk.s20124.tin.model.User;
import pjwstk.s20124.tin.model.payload.JwtResponse;
import pjwstk.s20124.tin.repository.UserRepository;
import pjwstk.s20124.tin.services.UserService;
import pjwstk.s20124.tin.model.payload.LoginRequest;

import java.util.List;

@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
@Validated
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final TokenProvider tokenProvider;
    private final UserService userService;


    @PostMapping
    public JwtResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername().toLowerCase(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User userDetails = (User) authentication.getPrincipal();

        String jwt = tokenProvider.generateJwtToken(userDetails);

        List<String> roles = userDetails.getRoles().stream()
            .map(Role::getName)
            .map(ERole::name)
            .toList();

        return new JwtResponse(jwt, "Bearer", userDetails.getId(),
            userDetails.getUsername(), userDetails.getEmail(), roles);
    }
}
