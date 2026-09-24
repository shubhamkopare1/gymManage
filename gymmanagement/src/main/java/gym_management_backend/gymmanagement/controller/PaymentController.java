package gym_management_backend.gymmanagement.controller;

import gym_management_backend.gymmanagement.dto.request.PaymentOrderRequestDto;
import gym_management_backend.gymmanagement.dto.request.PaymentRequestDto;
import gym_management_backend.gymmanagement.dto.request.PaymentVerificationRequestDto;
import gym_management_backend.gymmanagement.dto.response.PaymentOrderResponseDto;
import gym_management_backend.gymmanagement.dto.response.PaymentResponseDto;
import gym_management_backend.gymmanagement.entity.enums.PaymentStatus;
import gym_management_backend.gymmanagement.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/gym/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponseDto> createPayment(
            @Valid @RequestBody PaymentRequestDto request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        paymentService.createPayment(request)
                );
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponseDto>>
    getAllPayments() {

        return ResponseEntity.ok(
                paymentService.getAllPayments()
        );
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<PaymentResponseDto>>
    getMemberPayments(
            @PathVariable UUID memberId
    ) {

        return ResponseEntity.ok(
                paymentService.getMemberPayments(
                        memberId
                )
        );
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponseDto>
    getPaymentById(
            @PathVariable UUID paymentId
    ) {

        return ResponseEntity.ok(
                paymentService.getPaymentById(
                        paymentId
                )
        );
    }
    @PostMapping("/order")
    public ResponseEntity<PaymentOrderResponseDto> createPaymentOrder(
            @Valid @RequestBody PaymentOrderRequestDto request
    ) {

        return ResponseEntity.ok(
                paymentService.createPaymentOrder(request)
        );
    }
    @PostMapping("/verify")
    public ResponseEntity<PaymentResponseDto> verifyPayment(
            @Valid @RequestBody PaymentVerificationRequestDto request
    ) {

        return ResponseEntity.ok(
                paymentService.verifyPayment(request)
        );
    }

    @PostMapping(
            value = "/webhook",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> razorpayWebhook(
            @RequestBody String payload,
            @RequestHeader("X-Razorpay-Signature") String signature
    ) {

        paymentService.handleWebhook(
                payload,
                signature
        );

        return ResponseEntity.ok("Webhook processed");
    }
    @PutMapping("/{paymentId}/status")
    public ResponseEntity<PaymentResponseDto>
    updatePaymentStatus(
            @PathVariable UUID paymentId,
            @RequestParam PaymentStatus status
    ) {

        return ResponseEntity.ok(
                paymentService.updatePaymentStatus(
                        paymentId,
                        status
                )
        );
    }

}