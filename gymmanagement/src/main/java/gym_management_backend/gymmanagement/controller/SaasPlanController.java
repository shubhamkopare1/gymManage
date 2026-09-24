package gym_management_backend.gymmanagement.controller;

import gym_management_backend.gymmanagement.dto.request.SaasPlanRequestDto;
import gym_management_backend.gymmanagement.dto.response.SaasPlanResponseDto;
import gym_management_backend.gymmanagement.service.SaasPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/saas-plans")
@RequiredArgsConstructor
public class SaasPlanController {

    private final SaasPlanService saasPlanService;

    @PostMapping
    public ResponseEntity<SaasPlanResponseDto>
    createPlan(
            @Valid @RequestBody SaasPlanRequestDto request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        saasPlanService.createPlan(request)
                );
    }

    @GetMapping
    public ResponseEntity<List<SaasPlanResponseDto>>
    getAllPlans() {

        return ResponseEntity.ok(
                saasPlanService.getAllPlans()
        );
    }

    @GetMapping("/{planId}")
    public ResponseEntity<SaasPlanResponseDto>
    getPlanById(
            @PathVariable UUID planId
    ) {

        return ResponseEntity.ok(
                saasPlanService.getPlanById(planId)
        );
    }

    @PutMapping("/{planId}")
    public ResponseEntity<SaasPlanResponseDto>
    updatePlan(
            @PathVariable UUID planId,
            @Valid @RequestBody SaasPlanRequestDto request
    ) {

        return ResponseEntity.ok(
                saasPlanService.updatePlan(
                        planId,
                        request
                )
        );
    }

    @DeleteMapping("/{planId}")
    public ResponseEntity<Void>
    deletePlan(
            @PathVariable UUID planId
    ) {

        saasPlanService.deletePlan(planId);

        return ResponseEntity.noContent().build();
    }

}