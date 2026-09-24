package gym_management_backend.gymmanagement.dto.request;

import gym_management_backend.gymmanagement.entity.enums.SaasPlanType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaasPlanRequestDto {

    @NotNull(message = "Plan type is required")
    private SaasPlanType planType;

    @NotBlank(message = "Plan name is required")
    private String name;

    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", message = "Price cannot be negative")
    private BigDecimal price;

    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be at least 1 day")
    private Integer durationInDays;

    @NotNull(message = "Maximum members is required")
    @Min(value = 1, message = "Maximum members must be at least 1")
    private Integer maxMembers;

    @NotNull(message = "Maximum trainers is required")
    @Min(value = 1, message = "Maximum trainers must be at least 1")
    private Integer maxTrainers;
}