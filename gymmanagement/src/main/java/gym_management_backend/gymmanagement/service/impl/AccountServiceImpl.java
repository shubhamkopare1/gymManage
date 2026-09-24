package gym_management_backend.gymmanagement.service.impl;

import gym_management_backend.gymmanagement.dto.request.CreateAccountRequestDto;
import gym_management_backend.gymmanagement.dto.response.AccountResponseDto;
import gym_management_backend.gymmanagement.entity.*;
import gym_management_backend.gymmanagement.entity.enums.Role;
import gym_management_backend.gymmanagement.repository.MemberRepository;
import gym_management_backend.gymmanagement.repository.ReceptionistRepository;
import gym_management_backend.gymmanagement.repository.TrainerRepository;
import gym_management_backend.gymmanagement.repository.UserRepository;
import gym_management_backend.gymmanagement.security.AuthenticatedUser;
import gym_management_backend.gymmanagement.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final TrainerRepository trainerRepository;
    private final ReceptionistRepository receptionistRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AccountResponseDto createMemberAccount(
            UUID memberId,
            CreateAccountRequestDto request
    ) {

        UUID gymId = getGymId();

        Member member = memberRepository
                .findByIdAndGymId(memberId, gymId)
                .orElseThrow(() ->
                        new RuntimeException("Member not found"));

        if (member.getUser() != null) {
            throw new RuntimeException(
                    "Member already has a login account"
            );
        }

        User user = createUser(
                member.getName(),
                request,
                Role.MEMBER,
                member.getGym()
        );

        member.setUser(user);
        memberRepository.save(member);

        return mapToResponse(user);
    }

    @Override
    public AccountResponseDto createTrainerAccount(
            UUID trainerId,
            CreateAccountRequestDto request
    ) {

        UUID gymId = getGymId();

        Trainer trainer = trainerRepository
                .findByIdAndGymId(trainerId, gymId)
                .orElseThrow(() ->
                        new RuntimeException("Trainer not found"));

        if (trainer.getUser() != null) {
            throw new RuntimeException(
                    "Trainer already has a login account"
            );
        }

        User user = createUser(
                trainer.getName(),
                request,
                Role.TRAINER,
                trainer.getGym()
        );

        trainer.setUser(user);
        trainerRepository.save(trainer);

        return mapToResponse(user);
    }

    @Override
    public AccountResponseDto createReceptionistAccount(
            UUID receptionistId,
            CreateAccountRequestDto request
    ) {

        UUID gymId = getGymId();

        Receptionist receptionist = receptionistRepository
                .findByIdAndGymId(receptionistId, gymId)
                .orElseThrow(() ->
                        new RuntimeException("Receptionist not found"));

        if (receptionist.getUser() != null) {
            throw new RuntimeException(
                    "Receptionist already has a login account"
            );
        }

        User user = createUser(
                receptionist.getName(),
                request,
                Role.RECEPTIONIST,
                receptionist.getGym()
        );

        receptionist.setUser(user);
        receptionistRepository.save(receptionist);

        return mapToResponse(user);
    }

    private User createUser(
            String name,
            CreateAccountRequestDto request,
            Role role,
            Gym gym
    ) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException(
                    "Email already registered"
            );
        }

        User user = User.builder()
                .name(name)
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .gym(gym)
                .active(true)
                .build();

        return userRepository.save(user);
    }

    private UUID getGymId() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                !(authentication.getPrincipal()
                        instanceof AuthenticatedUser authenticatedUser)) {

            throw new RuntimeException(
                    "User is not authenticated"
            );
        }

        if (authenticatedUser.getGymId() == null) {
            throw new RuntimeException(
                    "Gym not found"
            );
        }

        return authenticatedUser.getGymId();
    }

    private AccountResponseDto mapToResponse(User user) {

        return AccountResponseDto.builder()
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .active(user.isActive())
                .gymId(
                        user.getGym() != null
                                ? user.getGym().getId()
                                : null
                )
                .gymName(
                        user.getGym() != null
                                ? user.getGym().getName()
                                : null
                )
                .build();
    }
}