package gym_management_backend.gymmanagement.dto.response;


import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DietMealResponseDto {

    private UUID id;

    private UUID dietPlanId;

    private String mealName;

    private String mealTime;

    private String foodItems;

    private Integer calories;

    private Double protein;

    private Double carbohydrates;

    private Double fats;

    private String instructions;
}