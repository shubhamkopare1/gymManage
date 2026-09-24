package gym_management_backend.gymmanagement.controller;

import gym_management_backend.gymmanagement.dto.request.SaasSubscriptionRequestDto;
import gym_management_backend.gymmanagement.dto.response.SaasSubscriptionResponseDto;
import gym_management_backend.gymmanagement.service.SaasSubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/gym/saas-subscriptions")
@RequiredArgsConstructor
public class SaasSubscriptionController {

    private final SaasSubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<SaasSubscriptionResponseDto>
    subscribe(
            @Valid @RequestBody
            SaasSubscriptionRequestDto request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        subscriptionService.subscribe(request)
                );
    }

    @GetMapping("/current")
    public ResponseEntity<SaasSubscriptionResponseDto>
    getCurrentSubscription() {

        return ResponseEntity.ok(
                subscriptionService
                        .getCurrentSubscription()
        );
    }

    @GetMapping("/history")
    public ResponseEntity<List<SaasSubscriptionResponseDto>>
    getSubscriptionHistory() {

        return ResponseEntity.ok(
                subscriptionService
                        .getSubscriptionHistory()
        );
    }

    @GetMapping("/{subscriptionId}")
    public ResponseEntity<SaasSubscriptionResponseDto>
    getSubscriptionById(
            @PathVariable UUID subscriptionId
    ) {

        return ResponseEntity.ok(
                subscriptionService
                        .getSubscriptionById(subscriptionId)
        );
    }

    @PutMapping("/{subscriptionId}/cancel")
    public ResponseEntity<SaasSubscriptionResponseDto>
    cancelSubscription(
            @PathVariable UUID subscriptionId
    ) {

        return ResponseEntity.ok(
                subscriptionService
                        .cancelSubscription(subscriptionId)
        );
    }
}
