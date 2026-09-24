package gym_management_backend.gymmanagement.service.impl;

import gym_management_backend.gymmanagement.dto.request.DietMealRequestDto;
import gym_management_backend.gymmanagement.dto.response.DietMealResponseDto;
import gym_management_backend.gymmanagement.entity.AuditAction;
import gym_management_backend.gymmanagement.entity.DietMeal;
import gym_management_backend.gymmanagement.entity.DietPlan;
import gym_management_backend.gymmanagement.exception.ResourceNotFoundException;
import gym_management_backend.gymmanagement.mapper.DietMealMapper;
import gym_management_backend.gymmanagement.repository.DietMealRepository;
import gym_management_backend.gymmanagement.repository.DietPlanRepository;
import gym_management_backend.gymmanagement.security.Auditable;
import gym_management_backend.gymmanagement.security.AuthenticatedUser;
import gym_management_backend.gymmanagement.service.DietMealService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DietMealServiceImpl
        implements DietMealService {

    private final DietMealRepository mealRepository;

    private final DietPlanRepository dietPlanRepository;

    @Override
    @Auditable(
            action = AuditAction.CREATE,
            entityType = "DIET_MEAL"
    )
    public DietMealResponseDto addMeal(
            DietMealRequestDto request
    ) {

        UUID gymId = getGymId();

        DietPlan dietPlan =
                dietPlanRepository
                        .findByIdAndGymId(
                                request.getDietPlanId(),
                                gymId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Diet plan not found"
                                )
                        );

        DietMeal meal = DietMeal.builder()
                .mealName(request.getMealName())
                .mealTime(request.getMealTime())
                .foodItems(request.getFoodItems())
                .calories(request.getCalories())
                .protein(request.getProtein())
                .carbohydrates(
                        request.getCarbohydrates()
                )
                .fats(request.getFats())
                .instructions(
                        request.getInstructions()
                )
                .dietPlan(dietPlan)
                .build();

        DietMeal savedMeal =
                mealRepository.save(meal);

        return DietMealMapper.toResponse(
                savedMeal
        );
    }

    @Override
    public List<DietMealResponseDto> getPlanMeals(
            UUID dietPlanId
    ) {

        UUID gymId = getGymId();

        dietPlanRepository
                .findByIdAndGymId(
                        dietPlanId,
                        gymId
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Diet plan not found"
                        )
                );

        return mealRepository
                .findAllByDietPlanId(dietPlanId)
                .stream()
                .map(DietMealMapper::toResponse)
                .toList();
    }

    @Override
    public DietMealResponseDto getMealById(
            UUID mealId
    ) {

        UUID gymId = getGymId();

        DietMeal meal =
                mealRepository
                        .findById(mealId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Meal not found"
                                )
                        );

        UUID planGymId =
                meal.getDietPlan()
                        .getGym()
                        .getId();

        if (!planGymId.equals(gymId)) {
            throw new ResourceNotFoundException(
                    "Meal not found"
            );
        }

        return DietMealMapper.toResponse(meal);
    }

    @Override
    @Auditable(
            action = AuditAction.DELETE,
            entityType = "DIET_MEAL"
    )
    public void deleteMeal(UUID mealId) {

        UUID gymId = getGymId();

        DietMeal meal =
                mealRepository
                        .findById(mealId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Meal not found"
                                )
                        );

        UUID planGymId =
                meal.getDietPlan()
                        .getGym()
                        .getId();

        if (!planGymId.equals(gymId)) {
            throw new ResourceNotFoundException(
                    "Meal not found"
            );
        }

        mealRepository.delete(meal);
    }

    private UUID getGymId() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        AuthenticatedUser authenticatedUser =
                (AuthenticatedUser)
                        authentication.getPrincipal();

        UUID gymId =
                authenticatedUser.getGymId();

        if (gymId == null) {
            throw new ResourceNotFoundException(
                    "User is not associated with any gym"
            );
        }

        return gymId;
    }
}