package gym_management_backend.gymmanagement.service.impl;

import gym_management_backend.gymmanagement.dto.request.WorkoutPlanRequestDto;
import gym_management_backend.gymmanagement.dto.response.WorkoutPlanResponseDto;
import gym_management_backend.gymmanagement.entity.*;
import gym_management_backend.gymmanagement.exception.ResourceNotFoundException;
import gym_management_backend.gymmanagement.mapper.WorkoutPlanMapper;
import gym_management_backend.gymmanagement.repository.GymRepository;
import gym_management_backend.gymmanagement.repository.MemberRepository;
import gym_management_backend.gymmanagement.repository.TrainerRepository;
import gym_management_backend.gymmanagement.repository.WorkoutPlanRepository;
import gym_management_backend.gymmanagement.security.Auditable;
import gym_management_backend.gymmanagement.security.AuthenticatedUser;
import gym_management_backend.gymmanagement.service.WorkoutPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkoutPlanServiceImpl
        implements WorkoutPlanService {

    private final WorkoutPlanRepository workoutPlanRepository;

    private final MemberRepository memberRepository;

    private final TrainerRepository trainerRepository;

    private final GymRepository gymRepository;

    @Override
    @Auditable(
            action = AuditAction.CREATE,
            entityType = "WORKOUT_PLAN"
    )
    public WorkoutPlanResponseDto createPlan(
            WorkoutPlanRequestDto request
    ) {

        UUID gymId = getGymId();

        // Find member inside current gym
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

        // Trainer is optional
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

        WorkoutPlan plan = WorkoutPlan.builder()
                .name(request.getName())
                .description(request.getDescription())
                .startDate(startDate)
                .endDate(request.getEndDate())
                .active(true)
                .member(member)
                .trainer(trainer)
                .gym(gym)
                .build();

        WorkoutPlan savedPlan =
                workoutPlanRepository.save(plan);

        return WorkoutPlanMapper.toResponse(
                savedPlan
        );
    }

    @Override
    public List<WorkoutPlanResponseDto> getAllPlans() {

        UUID gymId = getGymId();

        return workoutPlanRepository
                .findAllByGymId(gymId)
                .stream()
                .map(WorkoutPlanMapper::toResponse)
                .toList();
    }

    @Override
    public List<WorkoutPlanResponseDto> getMemberPlans(
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

        return workoutPlanRepository
                .findAllByMemberIdAndGymId(
                        memberId,
                        gymId
                )
                .stream()
                .map(WorkoutPlanMapper::toResponse)
                .toList();
    }

    @Override
    public WorkoutPlanResponseDto getPlanById(
            UUID planId
    ) {

        UUID gymId = getGymId();

        WorkoutPlan plan =
                workoutPlanRepository
                        .findByIdAndGymId(
                                planId,
                                gymId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Workout plan not found"
                                )
                        );

        return WorkoutPlanMapper.toResponse(plan);
    }

    @Override
    @Auditable(
            action = AuditAction.DELETE,
            entityType = "WORKOUT_PLAN"
    )
    public void deletePlan(UUID planId) {

        UUID gymId = getGymId();

        WorkoutPlan plan =
                workoutPlanRepository
                        .findByIdAndGymId(
                                planId,
                                gymId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Workout plan not found"
                                )
                        );

        workoutPlanRepository.delete(plan);
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