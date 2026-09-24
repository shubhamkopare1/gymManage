package gym_management_backend.gymmanagement.mapper;


import gym_management_backend.gymmanagement.dto.response.WorkoutExerciseResponseDto;
import gym_management_backend.gymmanagement.entity.WorkoutExercise;

public class WorkoutExerciseMapper {

    private WorkoutExerciseMapper() {
    }

    public static WorkoutExerciseResponseDto toResponse(
            WorkoutExercise exercise
    ) {

        return WorkoutExerciseResponseDto.builder()
                .id(exercise.getId())
                .workoutPlanId(
                        exercise.getWorkoutPlan().getId()
                )
                .exerciseName(
                        exercise.getExerciseName()
                )
                .day(exercise.getDay())
                .sets(exercise.getSets())
                .reps(exercise.getReps())
                .duration(exercise.getDuration())
                .restTime(exercise.getRestTime())
                .instructions(exercise.getInstructions())
                .build();
    }
}