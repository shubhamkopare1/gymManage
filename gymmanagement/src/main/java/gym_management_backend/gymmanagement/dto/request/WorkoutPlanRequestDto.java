package gym_management_backend.gymmanagement.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutPlanRequestDto {

    @NotBlank(message = "Workout plan name is required")
    private String name;

    private String description;

    @NotNull(message = "Member ID is required")
    private UUID memberId;

    private UUID trainerId;

    private LocalDate startDate;

    private LocalDate endDate;
}