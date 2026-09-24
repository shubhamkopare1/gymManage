package gym_management_backend.gymmanagement.service.impl;

import gym_management_backend.gymmanagement.dto.request.WorkoutExerciseRequestDto;
import gym_management_backend.gymmanagement.dto.response.WorkoutExerciseResponseDto;
import gym_management_backend.gymmanagement.entity.AuditAction;
import gym_management_backend.gymmanagement.entity.WorkoutExercise;
import gym_management_backend.gymmanagement.entity.WorkoutPlan;
import gym_management_backend.gymmanagement.exception.ResourceNotFoundException;
import gym_management_backend.gymmanagement.mapper.WorkoutExerciseMapper;
import gym_management_backend.gymmanagement.repository.WorkoutExerciseRepository;
import gym_management_backend.gymmanagement.repository.WorkoutPlanRepository;
import gym_management_backend.gymmanagement.security.Auditable;
import gym_management_backend.gymmanagement.security.AuthenticatedUser;
import gym_management_backend.gymmanagement.service.WorkoutExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkoutExerciseServiceImpl
        implements WorkoutExerciseService {

    private final WorkoutExerciseRepository exerciseRepository;

    private final WorkoutPlanRepository workoutPlanRepository;

    @Override
    @Auditable(
            action = AuditAction.CREATE,
            entityType = "WORKOUT_EXERCISE"
    )
    public WorkoutExerciseResponseDto addExercise(
            WorkoutExerciseRequestDto request
    ) {

        UUID gymId = getGymId();

        // Verify workout plan belongs to current gym
        WorkoutPlan plan =
                workoutPlanRepository
                        .findByIdAndGymId(
                                request.getWorkoutPlanId(),
                                gymId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Workout plan not found"
                                )
                        );

        WorkoutExercise exercise =
                WorkoutExercise.builder()
                        .exerciseName(
                                request.getExerciseName()
                        )
                        .day(request.getDay())
                        .sets(request.getSets())
                        .reps(request.getReps())
                        .duration(request.getDuration())
                        .restTime(request.getRestTime())
                        .instructions(
                                request.getInstructions()
                        )
                        .workoutPlan(plan)
                        .build();

        WorkoutExercise savedExercise =
                exerciseRepository.save(exercise);

        return WorkoutExerciseMapper.toResponse(
                savedExercise
        );
    }

    @Override
    public List<WorkoutExerciseResponseDto>
    getPlanExercises(UUID workoutPlanId) {

        UUID gymId = getGymId();

        // Verify plan belongs to gym
        workoutPlanRepository
                .findByIdAndGymId(
                        workoutPlanId,
                        gymId
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Workout plan not found"
                        )
                );

        return exerciseRepository
                .findAllByWorkoutPlanId(
                        workoutPlanId
                )
                .stream()
                .map(WorkoutExerciseMapper::toResponse)
                .toList();
    }

    @Override
    public WorkoutExerciseResponseDto getExerciseById(
            UUID exerciseId
    ) {

        UUID gymId = getGymId();

        WorkoutExercise exercise =
                exerciseRepository
                        .findById(exerciseId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Exercise not found"
                                )
                        );

        // Verify exercise's plan belongs to current gym
        UUID planGymId =
                exercise.getWorkoutPlan()
                        .getGym()
                        .getId();

        if (!planGymId.equals(gymId)) {
            throw new ResourceNotFoundException(
                    "Exercise not found"
            );
        }

        return WorkoutExerciseMapper.toResponse(
                exercise
        );
    }

    @Override
    @Auditable(
            action = AuditAction.DELETE,
            entityType = "WORKOUT_EXERCISE"
    )
    public void deleteExercise(UUID exerciseId) {

        UUID gymId = getGymId();

        WorkoutExercise exercise =
                exerciseRepository
                        .findById(exerciseId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Exercise not found"
                                )
                        );

        UUID planGymId =
                exercise.getWorkoutPlan()
                        .getGym()
                        .getId();

        if (!planGymId.equals(gymId)) {
            throw new ResourceNotFoundException(
                    "Exercise not found"
            );
        }

        exerciseRepository.delete(exercise);
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