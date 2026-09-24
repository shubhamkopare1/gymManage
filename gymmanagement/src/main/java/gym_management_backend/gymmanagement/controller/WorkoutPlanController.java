package gym_management_backend.gymmanagement.controller;

import gym_management_backend.gymmanagement.dto.request.WorkoutPlanRequestDto;
import gym_management_backend.gymmanagement.dto.response.WorkoutPlanResponseDto;
import gym_management_backend.gymmanagement.service.WorkoutPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/gym/workout-plans")
@RequiredArgsConstructor
public class WorkoutPlanController {

    private final WorkoutPlanService workoutPlanService;

    @PostMapping
    public ResponseEntity<WorkoutPlanResponseDto> createPlan(
            @Valid @RequestBody WorkoutPlanRequestDto request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        workoutPlanService.createPlan(request)
                );
    }

    @GetMapping
    public ResponseEntity<List<WorkoutPlanResponseDto>>
    getAllPlans() {

        return ResponseEntity.ok(
                workoutPlanService.getAllPlans()
        );
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<WorkoutPlanResponseDto>>
    getMemberPlans(
            @PathVariable UUID memberId
    ) {

        return ResponseEntity.ok(
                workoutPlanService.getMemberPlans(
                        memberId
                )
        );
    }

    @GetMapping("/{planId}")
    public ResponseEntity<WorkoutPlanResponseDto>
    getPlanById(
            @PathVariable UUID planId
    ) {

        return ResponseEntity.ok(
                workoutPlanService.getPlanById(
                        planId
                )
        );
    }

    @DeleteMapping("/{planId}")
    public ResponseEntity<Void> deletePlan(
            @PathVariable UUID planId
    ) {

        workoutPlanService.deletePlan(planId);

        return ResponseEntity.noContent().build();
    }
}