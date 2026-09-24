package gym_management_backend.gymmanagement.service.impl;

import gym_management_backend.gymmanagement.dto.request.MembershipPlanRequestDto;
import gym_management_backend.gymmanagement.dto.response.MembershipPlanResponseDto;
import gym_management_backend.gymmanagement.entity.AuditAction;
import gym_management_backend.gymmanagement.entity.Gym;
import gym_management_backend.gymmanagement.entity.MembershipPlan;
import gym_management_backend.gymmanagement.exception.ResourceNotFoundException;
import gym_management_backend.gymmanagement.mapper.MembershipPlanMapper;
import gym_management_backend.gymmanagement.repository.GymRepository;
import gym_management_backend.gymmanagement.repository.MembershipPlanRepository;
import gym_management_backend.gymmanagement.security.Auditable;
import gym_management_backend.gymmanagement.security.AuthenticatedUser;
import gym_management_backend.gymmanagement.service.MembershipPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MembershipPlanServiceImpl
        implements MembershipPlanService {

    private final MembershipPlanRepository planRepository;
    private final GymRepository gymRepository;
    @Auditable(
            action = AuditAction.CREATE,
            entityType = "MEMBERSHIP_PLAN"
    )
    @Override
    public MembershipPlanResponseDto addPlan(
            MembershipPlanRequestDto request
    ) {

        UUID gymId = getGymId();

        if (planRepository.existsByNameAndGymId(
                request.getName(),
                gymId
        )) {
            throw new RuntimeException(
                    "Plan with this name already exists"
            );
        }

        Gym gym = gymRepository.findById(gymId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Gym not found"
                        )
                );

        System.out.println("Name = " + request.getName());
        System.out.println("Price = " + request.getPrice());
        System.out.println("Duration = " + request.getDurationInDays());
        MembershipPlan plan =
                MembershipPlan.builder()
                        .name(request.getName())
                        .description(
                                request.getDescription()
                        )
                        .price(request.getPrice())
                        .durationInDays(
                                request.getDurationInDays()
                        )
                        .active(true)
                        .gym(gym)
                        .build();

        MembershipPlan savedPlan =
                planRepository.save(plan);

        return MembershipPlanMapper.toResponse(
                savedPlan
        );
    }

    @Override
    public List<MembershipPlanResponseDto>
    getAllPlans() {

        UUID gymId = getGymId();

        return planRepository
                .findAllByGymId(gymId)
                .stream()
                .map(MembershipPlanMapper::toResponse)
                .toList();
    }

    @Override
    public MembershipPlanResponseDto getPlanById(
            UUID planId
    ) {

        UUID gymId = getGymId();

        MembershipPlan plan =
                planRepository
                        .findByIdAndGymId(
                                planId,
                                gymId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Membership plan not found"
                                )
                        );

        return MembershipPlanMapper.toResponse(plan);
    }
    @Auditable(
            action = AuditAction.UPDATE,
            entityType = "MEMBERSHIP_PLAN"
    )
    @Override
    public MembershipPlanResponseDto updatePlan(
            UUID planId,
            MembershipPlanRequestDto request
    ) {

        UUID gymId = getGymId();

        MembershipPlan plan =
                planRepository
                        .findByIdAndGymId(
                                planId,
                                gymId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Membership plan not found"
                                )
                        );

        MembershipPlanMapper.updateEntity(
                plan,
                request
        );

        MembershipPlan updatedPlan =
                planRepository.save(plan);

        return MembershipPlanMapper.toResponse(
                updatedPlan
        );
    }
    @Auditable(
            action = AuditAction.DELETE,
            entityType = "MEMBERSHIP_PLAN"
    )
    @Override
    public void deletePlan(UUID planId) {

        UUID gymId = getGymId();

        MembershipPlan plan =
                planRepository
                        .findByIdAndGymId(
                                planId,
                                gymId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Membership plan not found"
                                )
                        );

        planRepository.delete(plan);
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