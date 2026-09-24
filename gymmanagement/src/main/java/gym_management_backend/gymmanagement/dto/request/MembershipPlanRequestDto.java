package gym_management_backend.gymmanagement.dto.request;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MembershipPlanRequestDto {

    @NotBlank(message = "Plan name is required")
    private String name;

    private String description;

    @DecimalMin(
            value = "0.0",
            inclusive = false,
            message = "Price must be greater than 0"
    )
    private BigDecimal price;

    @Min(
            value = 1,
            message = "Duration must be at least 1 day"
    )
    private Integer durationInDays;
}