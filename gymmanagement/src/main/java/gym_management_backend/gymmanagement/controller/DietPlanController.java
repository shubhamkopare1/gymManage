package gym_management_backend.gymmanagement.controller;

import gym_management_backend.gymmanagement.dto.request.DietPlanRequestDto;
import gym_management_backend.gymmanagement.dto.response.DietPlanResponseDto;
import gym_management_backend.gymmanagement.service.DietPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/gym/diet-plans")
@RequiredArgsConstructor
public class DietPlanController {

    private final DietPlanService dietPlanService;

    @PostMapping
    public ResponseEntity<DietPlanResponseDto> createPlan(
            @Valid @RequestBody DietPlanRequestDto request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        dietPlanService.createPlan(request)
                );
    }

    @GetMapping
    public ResponseEntity<List<DietPlanResponseDto>>
    getAllPlans() {

        return ResponseEntity.ok(
                dietPlanService.getAllPlans()
        );
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<DietPlanResponseDto>>
    getMemberPlans(
            @PathVariable UUID memberId
    ) {

        return ResponseEntity.ok(
                dietPlanService.getMemberPlans(
                        memberId
                )
        );
    }

    @GetMapping("/{planId}")
    public ResponseEntity<DietPlanResponseDto>
    getPlanById(
            @PathVariable UUID planId
    ) {

        return ResponseEntity.ok(
                dietPlanService.getPlanById(
                        planId
                )
        );
    }

    @DeleteMapping("/{planId}")
    public ResponseEntity<Void> deletePlan(
            @PathVariable UUID planId
    ) {

        dietPlanService.deletePlan(planId);

        return ResponseEntity.noContent().build();
    }
}
