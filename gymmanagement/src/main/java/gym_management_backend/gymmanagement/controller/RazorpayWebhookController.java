package gym_management_backend.gymmanagement.controller;


import gym_management_backend.gymmanagement.service.RazorpayWebhookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhooks")
@RequiredArgsConstructor
public class RazorpayWebhookController {

    private final RazorpayWebhookService razorpayWebhookService;

    @PostMapping("/razorpay")
    public ResponseEntity<String> handleRazorpayWebhook(
            @RequestHeader("X-Razorpay-Signature") String signature,
            @RequestBody String payload
    ) {

        razorpayWebhookService.handleWebhook(
                payload,
                signature
        );

        return ResponseEntity.ok("Webhook received");
    }
}