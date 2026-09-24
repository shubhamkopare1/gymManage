package gym_management_backend.gymmanagement.controller;

import gym_management_backend.gymmanagement.dto.response.SaasPlanResponseDto;
import gym_management_backend.gymmanagement.service.SaasPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/gym/saas-plans")
@RequiredArgsConstructor
public class GymSaasPlanController {

    private final SaasPlanService saasPlanService;

    @GetMapping
    public ResponseEntity<List<SaasPlanResponseDto>> getActivePlans() {

        return ResponseEntity.ok(
                saasPlanService.getActivePlans()
        );
    }
}