package gym_management_backend.gymmanagement.service.impl;


import gym_management_backend.gymmanagement.dto.request.LoginRequestDto;
import gym_management_backend.gymmanagement.dto.request.RegisterRequestDto;
import gym_management_backend.gymmanagement.dto.response.AuthResponseDto;
import gym_management_backend.gymmanagement.dto.response.UserResponseDto;
import gym_management_backend.gymmanagement.entity.Gym;
import gym_management_backend.gymmanagement.entity.User;
import gym_management_backend.gymmanagement.entity.enums.Role;
import gym_management_backend.gymmanagement.exception.ResourceNotFoundException;
import gym_management_backend.gymmanagement.mapper.UserMapper;
import gym_management_backend.gymmanagement.repository.GymRepository;
import gym_management_backend.gymmanagement.repository.UserRepository;
import gym_management_backend.gymmanagement.security.AuthenticatedUser;
import gym_management_backend.gymmanagement.security.JwtService;
import gym_management_backend.gymmanagement.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final GymRepository gymRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public AuthResponseDto register(RegisterRequestDto request) {

        // 1. Check user email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("User email already registered");
        }

        // 2. Check gym email
        if (gymRepository.existsByEmail(request.getGymEmail())) {
            throw new RuntimeException("Gym email already registered");
        }

        // 3. Create Gym
        Gym gym = Gym.builder()
                .name(request.getGymName())
                .email(request.getGymEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .pincode(request.getPincode())
                .active(true)
                .build();

        Gym savedGym = gymRepository.save(gym);

        // 4. Create Gym Owner
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(
                        passwordEncoder.encode(request.getPassword())
                )
                .role(Role.GYM_OWNER)
                .gym(savedGym)
                .active(true)
                .build();

        User savedUser = userRepository.save(user);

        // 5. Generate JWT
        String token = jwtService.generateToken(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getRole().name(),
                savedUser.getGym() != null
                        ? savedUser.getGym().getId()
                        : null
        );

        // 6. Convert Entity → Response DTO
        UserResponseDto userResponse =
                UserMapper.toResponse(savedUser);

        // 7. Return response
        return AuthResponseDto.builder()
                .token(token)
                .user(userResponse)
                .build();
    }

    @Override
    public AuthResponseDto login(LoginRequestDto request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Invalid email or password"
                        )
                );

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {
            throw new RuntimeException("Invalid email or password");
        }

        if (!user.isActive()) {
            throw new RuntimeException("User account is inactive");
        }

        String token = jwtService.generateToken(
                user.getId(),
                user.getEmail(),
                user.getRole().name(),
                user.getGym() != null
                        ? user.getGym().getId()
                        : null
        );

        UserResponseDto userResponse =
                UserMapper.toResponse(user);

        return AuthResponseDto.builder()
                .token(token)
                .user(userResponse)
                .build();
    }
    @Override
    public UserResponseDto getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        AuthenticatedUser authenticatedUser =
                (AuthenticatedUser) authentication.getPrincipal();

        User user = userRepository
                .findById(authenticatedUser.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        return UserMapper.toResponse(user);
    }
}