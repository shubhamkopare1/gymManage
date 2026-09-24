package gym_management_backend.gymmanagement.dto.response;


import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MembershipPlanResponseDto {

    private UUID id;

    private String name;

    private String description;

    private BigDecimal price;

    private Integer durationInDays;

    private boolean active;

    private UUID gymId;

    private String gymName;
}