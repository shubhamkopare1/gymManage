package gym_management_backend.gymmanagement.dto.response;


import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutExerciseResponseDto {

    private UUID id;

    private UUID workoutPlanId;

    private String exerciseName;

    private String day;

    private Integer sets;

    private Integer reps;

    private String duration;

    private String restTime;

    private String instructions;
}