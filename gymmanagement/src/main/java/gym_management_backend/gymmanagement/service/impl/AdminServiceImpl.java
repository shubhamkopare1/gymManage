package gym_management_backend.gymmanagement.service.impl;

import gym_management_backend.gymmanagement.dto.response.AdminDashboardResponseDto;
import gym_management_backend.gymmanagement.dto.response.AdminGymResponseDto;
import gym_management_backend.gymmanagement.dto.response.AdminUserResponseDto;
import gym_management_backend.gymmanagement.entity.Gym;
import gym_management_backend.gymmanagement.entity.User;
import gym_management_backend.gymmanagement.entity.enums.Role;
import gym_management_backend.gymmanagement.entity.enums.SaasSubscriptionStatus;
import gym_management_backend.gymmanagement.exception.ResourceNotFoundException;
import gym_management_backend.gymmanagement.repository.*;
import gym_management_backend.gymmanagement.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl
        implements AdminService {

    private final GymRepository gymRepository;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final TrainerRepository trainerRepository;
    private final ReceptionistRepository receptionistRepository;
    private final SaasSubscriptionRepository saasSubscriptionRepository;

    @Override
    public AdminDashboardResponseDto getDashboard() {

        long totalGyms =
                gymRepository.count();

        long activeGyms =
                gymRepository.countByActiveTrue();

        long inactiveGyms =
                gymRepository.countByActiveFalse();

        long totalUsers =
                userRepository.count();

        long totalMembers = memberRepository.count();

        long totalTrainers = trainerRepository.count();

        long totalReceptionists = receptionistRepository.count();
        long activeSubscriptions =
                saasSubscriptionRepository.countByStatus(
                        SaasSubscriptionStatus.ACTIVE
                );

        return AdminDashboardResponseDto.builder()
                .totalGyms(totalGyms)
                .activeGyms(activeGyms)
                .inactiveGyms(inactiveGyms)
                .totalUsers(totalUsers)
                .totalMembers(totalMembers)
                .totalTrainers(totalTrainers)
                .totalReceptionists(totalReceptionists)
                .activeSubscriptions(activeSubscriptions)
                .totalRevenue(BigDecimal.ZERO)
                .build();
    }

    @Override
    public List<AdminGymResponseDto> getAllGyms() {

        return gymRepository.findAll()
                .stream()
                .map(this::mapGym)
                .toList();
    }

    @Override
    public AdminGymResponseDto getGymById(
            UUID gymId
    ) {

        Gym gym = gymRepository.findById(gymId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Gym not found"
                        )
                );

        return mapGym(gym);
    }

    @Override
    public AdminGymResponseDto activateGym(
            UUID gymId
    ) {

        Gym gym = gymRepository.findById(gymId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Gym not found"
                        )
                );

        gym.setActive(true);

        return mapGym(
                gymRepository.save(gym)
        );
    }

    @Override
    public AdminGymResponseDto deactivateGym(
            UUID gymId
    ) {

        Gym gym = gymRepository.findById(gymId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Gym not found"
                        )
                );

        gym.setActive(false);

        return mapGym(
                gymRepository.save(gym)
        );
    }

    @Override
    public List<AdminUserResponseDto> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::mapUser)
                .toList();
    }

    @Override
    public List<AdminUserResponseDto> getUsersByGym(
            UUID gymId
    ) {

        gymRepository.findById(gymId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Gym not found"
                        )
                );

        return userRepository.findAllByGymId(gymId)
                .stream()
                .map(this::mapUser)
                .toList();
    }

    private AdminGymResponseDto mapGym(
            Gym gym
    ) {

        long totalMembers =
                memberRepository.countByGymId(
                        gym.getId()
                );

        long totalTrainers =
                trainerRepository.countByGymId(
                        gym.getId()
                );

        long totalReceptionists =
                receptionistRepository.countByGymId(
                        gym.getId()
                );

        return AdminGymResponseDto.builder()
                .id(gym.getId())
                .name(gym.getName())
                .email(gym.getEmail())
                .phone(gym.getPhone())
                .address(gym.getAddress())
                .city(gym.getCity())
                .state(gym.getState())
                .pincode(gym.getPincode())
                .logoUrl(gym.getLogoUrl())
                .active(gym.isActive())
                .totalMembers(totalMembers)
                .totalTrainers(totalTrainers)
                .totalReceptionists(totalReceptionists)
                .build();
    }

    private AdminUserResponseDto mapUser(
            User user
    ) {

        return AdminUserResponseDto.builder()
                .id(user.getId())
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