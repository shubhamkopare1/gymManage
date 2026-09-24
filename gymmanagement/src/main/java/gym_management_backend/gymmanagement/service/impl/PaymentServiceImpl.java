package gym_management_backend.gymmanagement.service.impl;

import com.razorpay.Order;
import com.razorpay.Utils;
import gym_management_backend.gymmanagement.dto.request.PaymentOrderRequestDto;
import gym_management_backend.gymmanagement.dto.request.PaymentRequestDto;
import gym_management_backend.gymmanagement.dto.request.PaymentVerificationRequestDto;
import gym_management_backend.gymmanagement.dto.response.PaymentOrderResponseDto;
import gym_management_backend.gymmanagement.dto.response.PaymentResponseDto;
import gym_management_backend.gymmanagement.entity.*;
import gym_management_backend.gymmanagement.entity.enums.PaymentMethod;
import gym_management_backend.gymmanagement.entity.enums.PaymentStatus;
import gym_management_backend.gymmanagement.entity.enums.SubscriptionStatus;
import gym_management_backend.gymmanagement.exception.ResourceNotFoundException;
import gym_management_backend.gymmanagement.mapper.PaymentMapper;
import gym_management_backend.gymmanagement.repository.*;
import gym_management_backend.gymmanagement.security.Auditable;
import gym_management_backend.gymmanagement.security.AuthenticatedUser;
import gym_management_backend.gymmanagement.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.razorpay.RazorpayClient;
import org.springframework.beans.factory.annotation.Value;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl
        implements PaymentService {

    private final PaymentRepository paymentRepository;

    private final MemberRepository memberRepository;

    private final MemberSubscriptionRepository
            subscriptionRepository;

    private final MembershipPlanRepository planRepository;

    private final GymRepository gymRepository;

    private final RazorpayClient razorpayClient;

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;
    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;
    @Value("${razorpay.webhook.secret}")
    private String razorpayWebhookSecret;
    @Override
    @Auditable(
            action = AuditAction.PAYMENT,
            entityType = "PAYMENT"
    )
    public PaymentResponseDto createPayment(
            PaymentRequestDto request
    ) {

        UUID gymId = getGymId();

        // 1. Find member inside current gym
        Member member = memberRepository
                .findByIdAndGymId(
                        request.getMemberId(),
                        gymId
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Member not found"
                        )
                );

        // 2. Find gym
        Gym gym = gymRepository
                .findById(gymId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Gym not found"
                        )
                );

        // 3. Optional subscription
        MemberSubscription subscription = null;

        if (request.getSubscriptionId() != null) {

            subscription = subscriptionRepository
                    .findByIdAndGymId(
                            request.getSubscriptionId(),
                            gymId
                    )
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Subscription not found"
                            )
                    );

            // Make sure subscription belongs to same member
            if (!subscription
                    .getMember()
                    .getId()
                    .equals(member.getId())) {

                throw new RuntimeException(
                        "Subscription does not belong to this member"
                );
            }
        }

        // 4. Create payment
        Payment payment = Payment.builder()
                .member(member)
                .subscription(subscription)
                .amount(request.getAmount())
                .paymentMethod(
                        request.getPaymentMethod()
                )
                .status(PaymentStatus.SUCCESS)
                .transactionId(
                        request.getTransactionId()
                )
                .notes(request.getNotes())
                .paymentDate(LocalDateTime.now())
                .gym(gym)
                .build();

        Payment savedPayment =
                paymentRepository.save(payment);

        return PaymentMapper.toResponse(
                savedPayment
        );
    }
    @Override
    public PaymentOrderResponseDto createPaymentOrder(
            PaymentOrderRequestDto request
    ) {

        UUID gymId = getGymId();

        MemberSubscription subscription =
                subscriptionRepository
                        .findByIdAndGymId(
                                request.getSubscriptionId(),
                                gymId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Subscription not found"
                                )
                        );

        MembershipPlan plan =
                planRepository
                        .findByIdAndGymId(
                                subscription
                                        .getMembershipPlan()
                                        .getId(),
                                gymId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Membership plan not found"
                                )
                        );

        if (!plan.isActive()) {
            throw new IllegalArgumentException(
                    "Membership plan is inactive"
            );
        }

        BigDecimal amount = plan.getPrice();

        long amountInPaise =
                amount
                        .multiply(BigDecimal.valueOf(100))
                        .longValueExact();

        try {

            JSONObject orderRequest = new JSONObject();

            orderRequest.put(
                    "amount",
                    amountInPaise
            );

            orderRequest.put(
                    "currency",
                    "INR"
            );

            orderRequest.put(
                    "receipt",
                    "sub_" + subscription.getId()
            );

            Order order =
                    razorpayClient
                            .orders
                            .create(orderRequest);

            String razorpayOrderId =
                    order.get("id");
            Payment payment =
                    Payment.builder()
                            .amount(amount)
                            .status(PaymentStatus.PENDING)
                            .paymentMethod(PaymentMethod.RAZORPAY)
                            .subscription(subscription)
                            .member(subscription.getMember())
                            .gym(subscription.getGym())
                            .gatewayOrderId(razorpayOrderId)
                            .paymentDate(LocalDateTime.now())
                            .build();

            Payment savedPayment =
                    paymentRepository.save(payment);

            PaymentOrderResponseDto response =
                    new PaymentOrderResponseDto();

            response.setPaymentId(
                    savedPayment.getId()
            );

            response.setRazorpayOrderId(
                    razorpayOrderId
            );

            response.setRazorpayKeyId(
                    razorpayKeyId
            );

            response.setAmount(amount);

            response.setCurrency("INR");

            response.setStatus(
                    PaymentStatus.PENDING.name()
            );

            return response;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to create Razorpay order",
                    e
            );
        }
    }
    @Override
    @Auditable(
            action = AuditAction.PAYMENT,
            entityType = "PAYMENT"
    )
    public PaymentResponseDto verifyPayment(
            PaymentVerificationRequestDto request
    ) {

        UUID gymId = getGymId();

        Payment payment =
                paymentRepository
                        .findByIdAndGymId(
                                request.getPaymentId(),
                                gymId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Payment not found"
                                )
                        );

        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            return PaymentMapper.toResponse(payment);
        }

        if (!request.getRazorpayOrderId()
                .equals(payment.getGatewayOrderId())) {

            throw new IllegalArgumentException(
                    "Invalid Razorpay order ID"
            );
        }

        if (!PaymentMethod.RAZORPAY
                .equals(payment.getPaymentMethod())) {

            throw new IllegalArgumentException(
                    "Invalid payment method"
            );
        }

        try {

            String payload =
                    request.getRazorpayOrderId()
                            + "|"
                            + request.getRazorpayPaymentId();

            boolean verified =
                    Utils.verifySignature(
                            payload,
                            request.getRazorpaySignature(),
                            razorpayKeySecret
                    );

            if (!verified) {

                throw new IllegalArgumentException(
                        "Invalid Razorpay payment signature"
                );
            }
            com.razorpay.Payment razorpayPayment =
                    razorpayClient.payments.fetch(
                            request.getRazorpayPaymentId()
                    );

            Integer razorpayAmount =
                    razorpayPayment.get("amount");

            long expectedAmount =
                    payment.getAmount()
                            .multiply(BigDecimal.valueOf(100))
                            .longValueExact();

            if (razorpayAmount == null ||
                    razorpayAmount.longValue() != expectedAmount) {

                throw new IllegalArgumentException(
                        "Payment amount does not match"
                );
            }
            String razorpayStatus =
                    razorpayPayment.get("status");

            if (!"captured".equalsIgnoreCase(razorpayStatus)) {

                throw new IllegalArgumentException(
                        "Razorpay payment is not captured"
                );
            }
            payment.setGatewayPaymentId(
                    request.getRazorpayPaymentId()
            );

            payment.setStatus(
                    PaymentStatus.SUCCESS
            );

            payment.setPaymentDate(
                    LocalDateTime.now()
            );

            Payment savedPayment =
                    paymentRepository.save(payment);

            MemberSubscription subscription =
                    payment.getSubscription();

            if (subscription != null) {

                subscription.setStatus(
                        SubscriptionStatus.ACTIVE
                );

                subscriptionRepository.save(
                        subscription
                );
            }

            return PaymentMapper.toResponse(
                    savedPayment
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Payment verification failed",
                    e
            );
        }
    }
    @Override
    @Auditable(
            action = AuditAction.PAYMENT,
            entityType = "PAYMENT"
    )
    public PaymentResponseDto handleWebhook(
            String payload,
            String signature
    ) {

        try {

            boolean verified =
                    Utils.verifyWebhookSignature(
                            payload,
                            signature,
                            razorpayWebhookSecret
                    );

            if (!verified) {
                throw new IllegalArgumentException(
                        "Invalid Razorpay webhook signature"
                );
            }

            JSONObject webhook = new JSONObject(payload);

            String event = webhook.optString("event");

            if (!event.equals("payment.captured")
                    && !event.equals("order.paid")) {

                return null;
            }

            JSONObject paymentEntity =
                    webhook
                            .getJSONObject("payload")
                            .getJSONObject("payment")
                            .getJSONObject("entity");

            String razorpayPaymentId =
                    paymentEntity.getString("id");

            String razorpayOrderId =
                    paymentEntity.getString("order_id");

            Integer razorpayAmount =
                    paymentEntity.optInt("amount");

            Payment payment =
                    paymentRepository
                            .findByGatewayOrderId(
                                    razorpayOrderId
                            )
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Payment not found for Razorpay order: "
                                                    + razorpayOrderId
                                    )
                            );

            if (payment.getStatus() == PaymentStatus.SUCCESS) {
                return PaymentMapper.toResponse(payment);
            }

            long expectedAmount =
                    payment.getAmount()
                            .multiply(BigDecimal.valueOf(100))
                            .longValueExact();

            if (razorpayAmount.longValue() != expectedAmount) {

                throw new IllegalArgumentException(
                        "Webhook payment amount does not match"
                );
            }

            String razorpayStatus =
                    paymentEntity.optString("status");

            if (!"captured".equalsIgnoreCase(razorpayStatus)) {

                throw new IllegalArgumentException(
                        "Razorpay payment is not captured"
                );
            }

            payment.setGatewayPaymentId(
                    razorpayPaymentId
            );

            payment.setStatus(
                    PaymentStatus.SUCCESS
            );

            payment.setPaymentDate(
                    LocalDateTime.now()
            );

            Payment savedPayment =
                    paymentRepository.save(payment);

            MemberSubscription subscription =
                    payment.getSubscription();

            if (subscription != null) {

                subscription.setStatus(
                        SubscriptionStatus.ACTIVE
                );

                subscriptionRepository.save(
                        subscription
                );
            }

            return PaymentMapper.toResponse(
                    savedPayment
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Razorpay webhook processing failed",
                    e
            );
        }
    }
    @Override
    public List<PaymentResponseDto> getAllPayments() {

        UUID gymId = getGymId();

        return paymentRepository
                .findAllByGymId(gymId)
                .stream()
                .map(PaymentMapper::toResponse)
                .toList();
    }

    @Override
    public List<PaymentResponseDto> getMemberPayments(
            UUID memberId
    ) {

        UUID gymId = getGymId();

        // Verify member belongs to gym
        memberRepository
                .findByIdAndGymId(
                        memberId,
                        gymId
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Member not found"
                        )
                );

        return paymentRepository
                .findAllByMemberIdAndGymId(
                        memberId,
                        gymId
                )
                .stream()
                .map(PaymentMapper::toResponse)
                .toList();
    }

    @Override
    public PaymentResponseDto getPaymentById(
            UUID paymentId
    ) {

        UUID gymId = getGymId();

        Payment payment =
                paymentRepository
                        .findByIdAndGymId(
                                paymentId,
                                gymId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Payment not found"
                                )
                        );

        return PaymentMapper.toResponse(payment);
    }

    @Override
    @Auditable(
            action = AuditAction.UPDATE,
            entityType = "PAYMENT"
    )
    public PaymentResponseDto updatePaymentStatus(
            UUID paymentId,
            PaymentStatus status
    ) {

        UUID gymId = getGymId();

        Payment payment =
                paymentRepository
                        .findByIdAndGymId(
                                paymentId,
                                gymId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Payment not found"
                                )
                        );

        payment.setStatus(status);

        Payment updatedPayment =
                paymentRepository.save(payment);

        return PaymentMapper.toResponse(
                updatedPayment
        );
    }

    private UUID getGymId() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        AuthenticatedUser authenticatedUser =
                (AuthenticatedUser)
                        authentication.getPrincipal();

        UUID gymId =
                authenticatedUser.getGymId();

        if (gymId == null) {
            throw new ResourceNotFoundException(
                    "User is not associated with any gym"
            );
        }

        return gymId;
    }
}
