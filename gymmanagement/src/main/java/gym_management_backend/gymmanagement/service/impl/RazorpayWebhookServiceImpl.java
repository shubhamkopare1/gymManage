package gym_management_backend.gymmanagement.service.impl;


import com.razorpay.Utils;
import gym_management_backend.gymmanagement.entity.MemberSubscription;
import gym_management_backend.gymmanagement.entity.Payment;
import gym_management_backend.gymmanagement.entity.enums.PaymentStatus;
import gym_management_backend.gymmanagement.entity.enums.SubscriptionStatus;
import gym_management_backend.gymmanagement.repository.PaymentRepository;
import gym_management_backend.gymmanagement.repository.MemberSubscriptionRepository;
import gym_management_backend.gymmanagement.service.RazorpayWebhookService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RazorpayWebhookServiceImpl implements RazorpayWebhookService {

    private final PaymentRepository paymentRepository;
    private final MemberSubscriptionRepository subscriptionRepository;

    @Value("${razorpay.webhook.secret}")
    private String webhookSecret;

    @Override
    public void handleWebhook(String payload, String signature) {

        try {

            boolean verified = Utils.verifyWebhookSignature(
                    payload,
                    signature,
                    webhookSecret
            );

            if (!verified) {
                throw new IllegalArgumentException(
                        "Invalid Razorpay webhook signature"
                );
            }

            JSONObject root = new JSONObject(payload);

            String event = root.optString("event");

            JSONObject paymentEntity = root
                    .getJSONObject("payload")
                    .getJSONObject("payment")
                    .getJSONObject("entity");

            String razorpayPaymentId =
                    paymentEntity.optString("id");

            String razorpayOrderId =
                    paymentEntity.optString("order_id");

            String razorpayStatus =
                    paymentEntity.optString("status");

            if ("payment.captured".equals(event)) {

                handlePaymentCaptured(
                        razorpayOrderId,
                        razorpayPaymentId,
                        razorpayStatus,
                        paymentEntity
                );

            } else if ("payment.failed".equals(event)) {

                handlePaymentFailed(
                        razorpayOrderId,
                        razorpayPaymentId
                );
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Webhook processing failed",
                    e
            );
        }
    }

    private void handlePaymentCaptured(
            String razorpayOrderId,
            String razorpayPaymentId,
            String razorpayStatus,
            JSONObject paymentEntity
    ) {

        if (!"captured".equalsIgnoreCase(razorpayStatus)) {
            throw new IllegalArgumentException(
                    "Razorpay payment is not captured"
            );
        }

        Payment payment = paymentRepository
                .findByGatewayOrderId(razorpayOrderId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Payment not found for Razorpay order: "
                                + razorpayOrderId
                ));

        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            return;
        }

        Integer razorpayAmount =
                paymentEntity.optInt("amount");

        long expectedAmount = payment
                .getAmount()
                .multiply(BigDecimal.valueOf(100))
                .longValueExact();

        if (razorpayAmount.longValue() != expectedAmount) {
            throw new IllegalArgumentException(
                    "Payment amount does not match"
            );
        }

        payment.setGatewayPaymentId(razorpayPaymentId);
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setPaymentDate(LocalDateTime.now());

        Payment savedPayment =
                paymentRepository.save(payment);

        MemberSubscription subscription =
                savedPayment.getSubscription();

        if (subscription != null) {

            subscription.setStatus(
                    SubscriptionStatus.ACTIVE
            );

            subscriptionRepository.save(subscription);
        }
    }

    private void handlePaymentFailed(
            String razorpayOrderId,
            String razorpayPaymentId
    ) {

        Payment payment = paymentRepository
                .findByGatewayOrderId(razorpayOrderId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Payment not found for Razorpay order: "
                                + razorpayOrderId
                ));

        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            return;
        }

        payment.setGatewayPaymentId(razorpayPaymentId);
        payment.setStatus(PaymentStatus.FAILED);
        payment.setPaymentDate(LocalDateTime.now());

        paymentRepository.save(payment);
    }
}