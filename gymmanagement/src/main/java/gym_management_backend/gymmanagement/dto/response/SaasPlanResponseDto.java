package gym_management_backend.gymmanagement.dto.response;

import gym_management_backend.gymmanagement.entity.enums.SaasPlanType;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaasPlanResponseDto {

    private UUID id;

    private SaasPlanType planType;

    private String name;

    private String description;

    private BigDecimal price;

    private Integer durationInDays;

    private Integer maxMembers;

    private Integer maxTrainers;

    private boolean active;
}