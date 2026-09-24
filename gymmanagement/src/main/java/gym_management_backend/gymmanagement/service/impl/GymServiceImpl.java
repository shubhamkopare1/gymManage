package gym_management_backend.gymmanagement.service.impl;

import gym_management_backend.gymmanagement.dto.request.GymRequestDto;
import gym_management_backend.gymmanagement.dto.response.GymResponseDto;
import gym_management_backend.gymmanagement.entity.AuditAction;
import gym_management_backend.gymmanagement.entity.Gym;
import gym_management_backend.gymmanagement.exception.ResourceNotFoundException;
import gym_management_backend.gymmanagement.mapper.GymMapper;
import gym_management_backend.gymmanagement.repository.GymRepository;
import gym_management_backend.gymmanagement.security.Auditable;
import gym_management_backend.gymmanagement.security.AuthenticatedUser;
import gym_management_backend.gymmanagement.service.GymService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GymServiceImpl implements GymService {

    private final GymRepository gymRepository;

    @Override
    public GymResponseDto getMyGym() {

        UUID gymId = getGymId();

        Gym gym = gymRepository.findById(gymId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Gym not found"
                        )
                );

        return GymMapper.toResponse(gym);
    }

    @Override
    @Auditable(
            action = AuditAction.UPDATE,
            entityType = "GYM"
    )
    public GymResponseDto updateMyGym(
            GymRequestDto request
    ) {

        UUID gymId = getGymId();

        Gym gym = gymRepository.findById(gymId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Gym not found"
                        )
                );

        GymMapper.updateEntity(gym, request);

        Gym updatedGym = gymRepository.save(gym);

        return GymMapper.toResponse(updatedGym);
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