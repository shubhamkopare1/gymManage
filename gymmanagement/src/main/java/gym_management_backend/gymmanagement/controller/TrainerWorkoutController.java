package gym_management_backend.gymmanagement.controller;

import gym_management_backend.gymmanagement.dto.response.WorkoutPlanResponseDto;
import gym_management_backend.gymmanagement.entity.Trainer;
import gym_management_backend.gymmanagement.entity.WorkoutPlan;
import gym_management_backend.gymmanagement.mapper.WorkoutPlanMapper;
import gym_management_backend.gymmanagement.repository.TrainerRepository;
import gym_management_backend.gymmanagement.repository.WorkoutPlanRepository;
import gym_management_backend.gymmanagement.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trainer/workout-plans")
@RequiredArgsConstructor
public class TrainerWorkoutController {

    private final TrainerRepository trainerRepository;
    private final WorkoutPlanRepository workoutPlanRepository;
    private final WorkoutPlanMapper workoutPlanMapper;

    @GetMapping
    public List<WorkoutPlanResponseDto> getMyWorkoutPlans() {

        AuthenticatedUser user = getUser();

        Trainer trainer = trainerRepository
                .findByUserId(user.getUserId())
                .orElseThrow(() ->
                        new RuntimeException("Trainer not found")
                );

        List<WorkoutPlan> plans =
                workoutPlanRepository
                        .findAllByTrainerIdAndGymId(
                                trainer.getId(),
                                user.getGymId()
                        );

        return plans.stream()
                .map(WorkoutPlanMapper::toResponse)
                .toList();
    }

    private AuthenticatedUser getUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        return (AuthenticatedUser)
                authentication.getPrincipal();
    }
}