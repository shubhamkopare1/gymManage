package gym_management_backend.gymmanagement.controller;

import gym_management_backend.gymmanagement.dto.request.MembershipPlanRequestDto;
import gym_management_backend.gymmanagement.dto.response.MembershipPlanResponseDto;
import gym_management_backend.gymmanagement.service.MembershipPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/gym/membership-plans")
@RequiredArgsConstructor
public class MembershipPlanController {

    private final MembershipPlanService planService;

    @PostMapping
    public ResponseEntity<MembershipPlanResponseDto>
    addPlan(
            @Valid @RequestBody
            MembershipPlanRequestDto request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        planService.addPlan(request)
                );
    }

    @GetMapping
    public ResponseEntity<List<MembershipPlanResponseDto>>
    getAllPlans() {

        return ResponseEntity.ok(
                planService.getAllPlans()
        );
    }

    @GetMapping("/{planId}")
    public ResponseEntity<MembershipPlanResponseDto>
    getPlanById(
            @PathVariable UUID planId
    ) {

        return ResponseEntity.ok(
                planService.getPlanById(planId)
        );
    }

    @PutMapping("/{planId}")
    public ResponseEntity<MembershipPlanResponseDto>
    updatePlan(
            @PathVariable UUID planId,
            @Valid @RequestBody
            MembershipPlanRequestDto request
    ) {

        return ResponseEntity.ok(
                planService.updatePlan(
                        planId,
                        request
                )
        );
    }

    @DeleteMapping("/{planId}")
    public ResponseEntity<Void> deletePlan(
            @PathVariable UUID planId
    ) {

        planService.deletePlan(planId);

        return ResponseEntity.noContent().build();
    }
}