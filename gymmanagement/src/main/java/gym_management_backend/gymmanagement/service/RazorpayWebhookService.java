package gym_management_backend.gymmanagement.service;


public interface RazorpayWebhookService {

    void handleWebhook(
            String payload,
            String signature
    );
}