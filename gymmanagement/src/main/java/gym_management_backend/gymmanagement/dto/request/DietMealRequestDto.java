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
public class DietMealRequestDto {

    @NotNull(message = "Diet plan ID is required")
    private UUID dietPlanId;

    @NotBlank(message = "Meal name is required")
    private String mealName;

    private String mealTime;

    private String foodItems;

    private Integer calories;

    private Double protein;

    private Double carbohydrates;

    private Double fats;

    private String instructions;
}