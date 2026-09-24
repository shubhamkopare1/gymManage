package gym_management_backend.gymmanagement.controller;

import gym_management_backend.gymmanagement.dto.request.SubscriptionRequestDto;
import gym_management_backend.gymmanagement.dto.response.SubscriptionResponseDto;
import gym_management_backend.gymmanagement.service.MemberSubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/gym/subscriptions")
@RequiredArgsConstructor
public class MemberSubscriptionController {

    private final MemberSubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<SubscriptionResponseDto>
    createSubscription(
            @Valid @RequestBody
            SubscriptionRequestDto request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        subscriptionService
                                .createSubscription(request)
                );
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionResponseDto>>
    getAllSubscriptions() {

        return ResponseEntity.ok(
                subscriptionService
                        .getAllSubscriptions()
        );
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<SubscriptionResponseDto>>
    getMemberSubscriptions(
            @PathVariable UUID memberId
    ) {

        return ResponseEntity.ok(
                subscriptionService
                        .getMemberSubscriptions(memberId)
        );
    }

    @GetMapping("/{subscriptionId}")
    public ResponseEntity<SubscriptionResponseDto>
    getSubscriptionById(
            @PathVariable UUID subscriptionId
    ) {

        return ResponseEntity.ok(
                subscriptionService
                        .getSubscriptionById(
                                subscriptionId
                        )
        );
    }

    @PutMapping("/{subscriptionId}/cancel")
    public ResponseEntity<Void>
    cancelSubscription(
            @PathVariable UUID subscriptionId
    ) {

        subscriptionService.cancelSubscription(
                subscriptionId
        );

        return ResponseEntity.noContent().build();
    }
}