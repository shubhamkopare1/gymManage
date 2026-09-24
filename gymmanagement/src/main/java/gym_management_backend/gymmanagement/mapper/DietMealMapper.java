package gym_management_backend.gymmanagement.mapper;


import gym_management_backend.gymmanagement.dto.response.DietMealResponseDto;
import gym_management_backend.gymmanagement.entity.DietMeal;

public class DietMealMapper {

    private DietMealMapper() {
    }

    public static DietMealResponseDto toResponse(
            DietMeal meal
    ) {

        return DietMealResponseDto.builder()
                .id(meal.getId())

                .dietPlanId(
                        meal.getDietPlan().getId()
                )

                .mealName(
                        meal.getMealName()
                )

                .mealTime(
                        meal.getMealTime()
                )

                .foodItems(
                        meal.getFoodItems()
                )

                .calories(
                        meal.getCalories()
                )

                .protein(
                        meal.getProtein()
                )

                .carbohydrates(
                        meal.getCarbohydrates()
                )

                .fats(
                        meal.getFats()
                )

                .instructions(
                        meal.getInstructions()
                )

                .build();
    }
}