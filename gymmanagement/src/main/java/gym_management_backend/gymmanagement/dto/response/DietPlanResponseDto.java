package gym_management_backend.gymmanagement.dto.response;


import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DietPlanResponseDto {

    private UUID id;

    private String name;

    private String description;

    private String goal;

    private LocalDate startDate;

    private LocalDate endDate;

    private boolean active;

    private UUID memberId;

    private String memberName;

    private UUID trainerId;

    private String trainerName;

    private UUID gymId;

    private String gymName;
}