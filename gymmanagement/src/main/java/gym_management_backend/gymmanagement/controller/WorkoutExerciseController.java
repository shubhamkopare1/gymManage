package gym_management_backend.gymmanagement.controller;

import gym_management_backend.gymmanagement.dto.request.WorkoutExerciseRequestDto;
import gym_management_backend.gymmanagement.dto.response.WorkoutExerciseResponseDto;
import gym_management_backend.gymmanagement.service.WorkoutExerciseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/gym/workout-exercises")
@RequiredArgsConstructor
public class WorkoutExerciseController {

    private final WorkoutExerciseService exerciseService;

    @PostMapping
    public ResponseEntity<WorkoutExerciseResponseDto> addExercise(
            @Valid @RequestBody WorkoutExerciseRequestDto request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        exerciseService.addExercise(request)
                );
    }

    @GetMapping("/plan/{workoutPlanId}")
    public ResponseEntity<List<WorkoutExerciseResponseDto>>
    getPlanExercises(
            @PathVariable UUID workoutPlanId
    ) {

        return ResponseEntity.ok(
                exerciseService.getPlanExercises(
                        workoutPlanId
                )
        );
    }

    @GetMapping("/{exerciseId}")
    public ResponseEntity<WorkoutExerciseResponseDto>
    getExerciseById(
            @PathVariable UUID exerciseId
    ) {

        return ResponseEntity.ok(
                exerciseService.getExerciseById(
                        exerciseId
                )
        );
    }

    @DeleteMapping("/{exerciseId}")
    public ResponseEntity<Void> deleteExercise(
            @PathVariable UUID exerciseId
    ) {

        exerciseService.deleteExercise(exerciseId);

        return ResponseEntity.noContent().build();
    }
}
