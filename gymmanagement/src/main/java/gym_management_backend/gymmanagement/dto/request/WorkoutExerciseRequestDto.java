package gym_management_backend.gymmanagement.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutExerciseRequestDto {

    @NotNull(message = "Workout plan ID is required")
    private UUID workoutPlanId;

    @NotBlank(message = "Exercise name is required")
    private String exerciseName;

    private String day;

    private Integer sets;

    private Integer reps;

    private String duration;

    private String restTime;

    private String instructions;
}