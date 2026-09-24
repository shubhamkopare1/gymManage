package gym_management_backend.gymmanagement.service;


import gym_management_backend.gymmanagement.dto.request.WorkoutExerciseRequestDto;
import gym_management_backend.gymmanagement.dto.response.WorkoutExerciseResponseDto;

import java.util.List;
import java.util.UUID;

public interface WorkoutExerciseService {

    WorkoutExerciseResponseDto addExercise(
            WorkoutExerciseRequestDto request
    );

    List<WorkoutExerciseResponseDto> getPlanExercises(
            UUID workoutPlanId
    );

    WorkoutExerciseResponseDto getExerciseById(
            UUID exerciseId
    );

    void deleteExercise(UUID exerciseId);
}
