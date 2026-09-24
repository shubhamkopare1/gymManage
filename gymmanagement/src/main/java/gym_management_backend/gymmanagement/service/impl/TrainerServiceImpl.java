package gym_management_backend.gymmanagement.service.impl;

import gym_management_backend.gymmanagement.dto.request.TrainerRequestDto;
import gym_management_backend.gymmanagement.dto.response.MemberResponseDto;
import gym_management_backend.gymmanagement.dto.response.TrainerResponseDto;
import gym_management_backend.gymmanagement.entity.*;
import gym_management_backend.gymmanagement.entity.enums.Role;
import gym_management_backend.gymmanagement.exception.ResourceNotFoundException;
import gym_management_backend.gymmanagement.mapper.MemberMapper;
import gym_management_backend.gymmanagement.mapper.TrainerMapper;
import gym_management_backend.gymmanagement.repository.GymRepository;
import gym_management_backend.gymmanagement.repository.MemberRepository;
import gym_management_backend.gymmanagement.repository.TrainerRepository;
import gym_management_backend.gymmanagement.repository.UserRepository;
import gym_management_backend.gymmanagement.security.Auditable;
import gym_management_backend.gymmanagement.security.AuthenticatedUser;
import gym_management_backend.gymmanagement.service.TrainerService;
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
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepository trainerRepository;
    private final GymRepository gymRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
  private final MemberRepository memberRepository;

    @Auditable(
            action = AuditAction.CREATE,
            entityType = "TRAINER"
    )
    @Override
    public TrainerResponseDto addTrainer(
            TrainerRequestDto request
    ) {

        UUID gymId = getGymId();

        // Check trainer email inside current gym
        if (trainerRepository.existsByEmailAndGymId(
                request.getEmail(),
                gymId
        )) {
            throw new RuntimeException(
                    "Trainer email already exists in this gym"
            );
        }

        // Get current gym
        Gym gym = gymRepository.findById(gymId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Gym not found"
                        )
                );

        // Create trainer profile
        Trainer trainer = Trainer.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .specialization(
                        request.getSpecialization()
                )
                .experience(
                        request.getExperience()
                )
                .joiningDate(
                        request.getJoiningDate() != null
                                ? request.getJoiningDate()
                                : LocalDate.now()
                )
                .address(request.getAddress())
                .active(true)
                .gym(gym)
                .build();

        // Save trainer first
        Trainer savedTrainer =
                trainerRepository.save(trainer);


        // ================================
        // CREATE LOGIN ACCOUNT IF PROVIDED
        // ================================

        if (request.getLoginEmail() != null
                && !request.getLoginEmail().isBlank()
                && request.getLoginPassword() != null
                && !request.getLoginPassword().isBlank()) {

            // Login email must be globally unique
            if (userRepository.existsByEmail(
                    request.getLoginEmail()
            )) {
                throw new RuntimeException(
                        "Login email already registered"
                );
            }

            // Create User account
            User user = User.builder()
                    .name(savedTrainer.getName())
                    .email(request.getLoginEmail())
                    .password(
                            passwordEncoder.encode(
                                    request.getLoginPassword()
                            )
                    )
                    .role(Role.TRAINER)
                    .gym(gym)
                    .active(true)
                    .build();

            User savedUser =
                    userRepository.save(user);

            // Connect Trainer with User
            savedTrainer.setUser(savedUser);

            trainerRepository.save(savedTrainer);
        }

        return TrainerMapper.toResponse(savedTrainer);
    }


    @Override
    public List<TrainerResponseDto> getAllTrainers() {

        UUID gymId = getGymId();

        return trainerRepository
                .findAllByGymId(gymId)
                .stream()
                .map(TrainerMapper::toResponse)
                .toList();
    }
    @Override
    public List<MemberResponseDto> getMyMembers() {

        UUID gymId = getGymId();

        UUID userId = getCurrentUserId();

        Trainer trainer =
                trainerRepository
                        .findByUserId(userId)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Trainer profile not found"
                                )
                        );

        List<Member> members =
                memberRepository
                        .findAllByTrainerIdAndGymId(
                                trainer.getId(),
                                gymId
                        );

        return members.stream()
                .map(MemberMapper::toResponse)
                .toList();
    }
    private UUID getCurrentUserId() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                !(authentication.getPrincipal()
                        instanceof AuthenticatedUser user)) {

            throw new RuntimeException(
                    "Authenticated user not found"
            );
        }

        return user.getUserId();
    }
    @Override
    public TrainerResponseDto getTrainerById(
            UUID trainerId
    ) {

        UUID gymId = getGymId();

        Trainer trainer = trainerRepository
                .findByIdAndGymId(
                        trainerId,
                        gymId
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Trainer not found"
                        )
                );

        return TrainerMapper.toResponse(trainer);
    }


    @Auditable(
            action = AuditAction.UPDATE,
            entityType = "TRAINER"
    )
    @Override
    public TrainerResponseDto updateTrainer(
            UUID trainerId,
            TrainerRequestDto request
    ) {

        UUID gymId = getGymId();

        Trainer trainer = trainerRepository
                .findByIdAndGymId(
                        trainerId,
                        gymId
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Trainer not found"
                        )
                );

        TrainerMapper.updateEntity(
                trainer,
                request
        );

        Trainer updatedTrainer =
                trainerRepository.save(trainer);

        return TrainerMapper.toResponse(
                updatedTrainer
        );
    }
    @Auditable(
            action = AuditAction.DELETE,
            entityType = "TRAINER"
    )
    @Override
    public void deleteTrainer(
            UUID trainerId
    ) {

        UUID gymId = getGymId();

        Trainer trainer = trainerRepository
                .findByIdAndGymId(
                        trainerId,
                        gymId
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Trainer not found"
                        )
                );

        trainerRepository.delete(trainer);
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