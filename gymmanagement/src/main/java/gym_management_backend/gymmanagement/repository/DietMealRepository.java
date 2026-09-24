package gym_management_backend.gymmanagement.repository;

import gym_management_backend.gymmanagement.entity.DietMeal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DietMealRepository
        extends JpaRepository<DietMeal, UUID> {

    List<DietMeal> findAllByDietPlanId(
            UUID dietPlanId
    );

    Optional<DietMeal> findByIdAndDietPlanId(
            UUID mealId,
            UUID dietPlanId
    );
}