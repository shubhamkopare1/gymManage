package gym_management_backend.gymmanagement.controller;

import gym_management_backend.gymmanagement.dto.request.DietMealRequestDto;
import gym_management_backend.gymmanagement.dto.response.DietMealResponseDto;
import gym_management_backend.gymmanagement.service.DietMealService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/gym/diet-meals")
@RequiredArgsConstructor
public class DietMealController {

    private final DietMealService mealService;

    @PostMapping
    public ResponseEntity<DietMealResponseDto> addMeal(
            @Valid @RequestBody DietMealRequestDto request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        mealService.addMeal(request)
                );
    }

    @GetMapping("/plan/{dietPlanId}")
    public ResponseEntity<List<DietMealResponseDto>>
    getPlanMeals(
            @PathVariable UUID dietPlanId
    ) {

        return ResponseEntity.ok(
                mealService.getPlanMeals(
                        dietPlanId
                )
        );
    }

    @GetMapping("/{mealId}")
    public ResponseEntity<DietMealResponseDto>
    getMealById(
            @PathVariable UUID mealId
    ) {

        return ResponseEntity.ok(
                mealService.getMealById(
                        mealId
                )
        );
    }

    @DeleteMapping("/{mealId}")
    public ResponseEntity<Void> deleteMeal(
            @PathVariable UUID mealId
    ) {

        mealService.deleteMeal(mealId);

        return ResponseEntity.noContent().build();
    }
}
