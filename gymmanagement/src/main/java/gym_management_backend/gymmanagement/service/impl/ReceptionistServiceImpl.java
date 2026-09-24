package gym_management_backend.gymmanagement.service.impl;


import gym_management_backend.gymmanagement.dto.request.ReceptionistRequestDto;
import gym_management_backend.gymmanagement.dto.response.ReceptionistResponseDto;
import gym_management_backend.gymmanagement.entity.AuditAction;
import gym_management_backend.gymmanagement.entity.Gym;
import gym_management_backend.gymmanagement.entity.Receptionist;
import gym_management_backend.gymmanagement.entity.User;
import gym_management_backend.gymmanagement.entity.enums.Role;
import gym_management_backend.gymmanagement.exception.ResourceNotFoundException;
import gym_management_backend.gymmanagement.mapper.ReceptionistMapper;
import gym_management_backend.gymmanagement.repository.GymRepository;
import gym_management_backend.gymmanagement.repository.ReceptionistRepository;
import gym_management_backend.gymmanagement.repository.UserRepository;
import gym_management_backend.gymmanagement.security.Auditable;
import gym_management_backend.gymmanagement.security.AuthenticatedUser;
import gym_management_backend.gymmanagement.service.ReceptionistService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReceptionistServiceImpl
        implements ReceptionistService {

    private final ReceptionistRepository receptionistRepository;
    private final GymRepository gymRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Auditable(
            action = AuditAction.CREATE,
            entityType = "RECEPTIONIST"
    )
    @Override
    public ReceptionistResponseDto addReceptionist(
            ReceptionistRequestDto request
    ) {

        UUID gymId = getGymId();

        if (receptionistRepository.existsByEmailAndGymId(
                request.getEmail(),
                gymId
        )) {
            throw new RuntimeException(
                    "Receptionist email already exists in this gym"
            );
        }

        Gym gym = gymRepository.findById(gymId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Gym not found"
                        )
                );

        Receptionist receptionist =
                Receptionist.builder()
                        .name(request.getName())
                        .email(request.getEmail())
                        .phone(request.getPhone())
                        .address(request.getAddress())
                        .joiningDate(
                                request.getJoiningDate() != null
                                        ? request.getJoiningDate()
                                        : LocalDate.now()
                        )
                        .active(true)
                        .gym(gym)
                        .build();

        Receptionist savedReceptionist =
                receptionistRepository.save(receptionist);

        // Create login account
        if (request.getLoginEmail() != null
                && !request.getLoginEmail().isBlank()
                && request.getLoginPassword() != null
                && !request.getLoginPassword().isBlank()) {

            if (userRepository.existsByEmail(
                    request.getLoginEmail()
            )) {
                throw new RuntimeException(
                        "Login email already registered"
                );
            }

            User user = User.builder()
                    .name(savedReceptionist.getName())
                    .email(request.getLoginEmail())
                    .password(
                            passwordEncoder.encode(
                                    request.getLoginPassword()
                            )
                    )
                    .role(Role.RECEPTIONIST)
                    .gym(gym)
                    .active(true)
                    .build();

            User savedUser =
                    userRepository.save(user);

            savedReceptionist.setUser(savedUser);

            receptionistRepository.save(savedReceptionist);
        }

        return ReceptionistMapper.toResponse(
                savedReceptionist
        );
    }

    @Override
    public List<ReceptionistResponseDto>
    getAllReceptionists() {

        UUID gymId = getGymId();

        return receptionistRepository
                .findAllByGymId(gymId)
                .stream()
                .map(ReceptionistMapper::toResponse)
                .toList();
    }

    @Override
    public ReceptionistResponseDto getReceptionistById(
            UUID receptionistId
    ) {

        UUID gymId = getGymId();

        Receptionist receptionist =
                receptionistRepository
                        .findByIdAndGymId(
                                receptionistId,
                                gymId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Receptionist not found"
                                )
                        );

        return ReceptionistMapper.toResponse(
                receptionist
        );
    }
    @Auditable(
            action = AuditAction.UPDATE,
            entityType = "RECEPTIONIST"
    )
    @Override
    public ReceptionistResponseDto updateReceptionist(
            UUID receptionistId,
            ReceptionistRequestDto request
    ) {

        UUID gymId = getGymId();

        Receptionist receptionist =
                receptionistRepository
                        .findByIdAndGymId(
                                receptionistId,
                                gymId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Receptionist not found"
                                )
                        );

        ReceptionistMapper.updateEntity(
                receptionist,
                request
        );

        Receptionist updatedReceptionist =
                receptionistRepository.save(receptionist);

        return ReceptionistMapper.toResponse(
                updatedReceptionist
        );
    }
    @Auditable(
            action = AuditAction.DELETE,
            entityType = "RECEPTIONIST"
    )
    @Override
    public void deleteReceptionist(
            UUID receptionistId
    ) {

        UUID gymId = getGymId();

        Receptionist receptionist =
                receptionistRepository
                        .findByIdAndGymId(
                                receptionistId,
                                gymId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Receptionist not found"
                                )
                        );

        receptionistRepository.delete(receptionist);
    }

    private UUID getGymId() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        AuthenticatedUser authenticatedUser =
                (AuthenticatedUser)
                        authentication.getPrincipal();

        UUID gymId = authenticatedUser.getGymId();

        if (gymId == null) {
            throw new ResourceNotFoundException(
                    "User is not associated with any gym"
            );
        }

        return gymId;
    }
}