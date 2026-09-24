package gym_management_backend.gymmanagement.controller;


import gym_management_backend.gymmanagement.dto.request.LoginRequestDto;
import gym_management_backend.gymmanagement.dto.request.RegisterRequestDto;
import gym_management_backend.gymmanagement.dto.response.AuthResponseDto;
import gym_management_backend.gymmanagement.dto.response.UserResponseDto;
import gym_management_backend.gymmanagement.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(
            @Valid @RequestBody RegisterRequestDto request
    ) {

        AuthResponseDto response =
                authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(
            @Valid @RequestBody LoginRequestDto request
    ) {

        AuthResponseDto response =
                authService.login(request);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getCurrentUser() {

        return ResponseEntity.ok(
                authService.getCurrentUser()
        );
    }
}
