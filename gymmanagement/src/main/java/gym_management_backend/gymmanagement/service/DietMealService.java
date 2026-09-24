package gym_management_backend.gymmanagement.service;


import gym_management_backend.gymmanagement.dto.request.DietMealRequestDto;
import gym_management_backend.gymmanagement.dto.response.DietMealResponseDto;

import java.util.List;
import java.util.UUID;

public interface DietMealService {

    DietMealResponseDto addMeal(
            DietMealRequestDto request
    );

    List<DietMealResponseDto> getPlanMeals(
            UUID dietPlanId
    );

    DietMealResponseDto getMealById(
            UUID mealId
    );

    void deleteMeal(UUID mealId);
}
