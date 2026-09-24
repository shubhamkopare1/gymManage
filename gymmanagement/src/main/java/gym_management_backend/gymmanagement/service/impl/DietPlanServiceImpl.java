package gym_management_backend.gymmanagement.service.impl;

import gym_management_backend.gymmanagement.dto.request.DietPlanRequestDto;
import gym_management_backend.gymmanagement.dto.response.DietPlanResponseDto;
import gym_management_backend.gymmanagement.entity.*;
import gym_management_backend.gymmanagement.exception.ResourceNotFoundException;
import gym_management_backend.gymmanagement.mapper.DietPlanMapper;
import gym_management_backend.gymmanagement.repository.DietPlanRepository;
import gym_management_backend.gymmanagement.repository.GymRepository;
import gym_management_backend.gymmanagement.repository.MemberRepository;
import gym_management_backend.gymmanagement.repository.TrainerRepository;
import gym_management_backend.gymmanagement.security.Auditable;
import gym_management_backend.gymmanagement.security.AuthenticatedUser;
import gym_management_backend.gymmanagement.service.DietPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DietPlanServiceImpl
        implements DietPlanService {

    private final DietPlanRepository dietPlanRepository;

    private final MemberRepository memberRepository;

    private final TrainerRepository trainerRepository;

    private final GymRepository gymRepository;

    @Override
    @Auditable(
            action = AuditAction.CREATE,
            entityType = "DIET_PLAN"
    )
    public DietPlanResponseDto createPlan(
            DietPlanRequestDto request
    ) {

        UUID gymId = getGymId();

        Member member = memberRepository
                .findByIdAndGymId(
                        request.getMemberId(),
                        gymId
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Member not found"
                        )
                );

        Trainer trainer = null;

        if (request.getTrainerId() != null) {

            trainer = trainerRepository
                    .findByIdAndGymId(
                            request.getTrainerId(),
                            gymId
                    )
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Trainer not found"
                            )
                    );
        }

        Gym gym = gymRepository
                .findById(gymId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Gym not found"
                        )
                );

        LocalDate startDate =
                request.getStartDate() != null
                        ? request.getStartDate()
                        : LocalDate.now();

        DietPlan plan = DietPlan.builder()
                .name(request.getName())
                .description(request.getDescription())
                .goal(request.getGoal())
                .startDate(startDate)
                .endDate(request.getEndDate())
                .active(true)
                .member(member)
                .trainer(trainer)
                .gym(gym)
                .build();

        DietPlan savedPlan =
                dietPlanRepository.save(plan);

        return DietPlanMapper.toResponse(
                savedPlan
        );
    }

    @Override
    public List<DietPlanResponseDto> getAllPlans() {

        UUID gymId = getGymId();

        return dietPlanRepository
                .findAllByGymId(gymId)
                .stream()
                .map(DietPlanMapper::toResponse)
                .toList();
    }

    @Override
    public List<DietPlanResponseDto> getMemberPlans(
            UUID memberId
    ) {

        UUID gymId = getGymId();

        memberRepository
                .findByIdAndGymId(
                        memberId,
                        gymId
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Member not found"
                        )
                );

        return dietPlanRepository
                .findAllByMemberIdAndGymId(
                        memberId,
                        gymId
                )
                .stream()
                .map(DietPlanMapper::toResponse)
                .toList();
    }

    @Override
    public DietPlanResponseDto getPlanById(
            UUID planId
    ) {

        UUID gymId = getGymId();

        DietPlan plan =
                dietPlanRepository
                        .findByIdAndGymId(
                                planId,
                                gymId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Diet plan not found"
                                )
                        );

        return DietPlanMapper.toResponse(plan);
    }

    @Override
    @Auditable(
            action = AuditAction.DELETE,
            entityType = "DIET_PLAN"
    )
    public void deletePlan(UUID planId) {

        UUID gymId = getGymId();

        DietPlan plan =
                dietPlanRepository
                        .findByIdAndGymId(
                                planId,
                                gymId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Diet plan not found"
                                )
                        );

        dietPlanRepository.delete(plan);
    }

    private UUID getGymId() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        AuthenticatedUser authenticatedUser =
                (AuthenticatedUser)
                        authentication.getPrincipal();

        UUID gymId =
                authenticatedUser.getGymId();

        if (gymId == null) {
            throw new ResourceNotFoundException(
                    "User is not associated with any gym"
            );
        }

        return gymId;
    }
}