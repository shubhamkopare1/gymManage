package gym_management_backend.gymmanagement.dto.response;

import gym_management_backend.gymmanagement.entity.enums.PaymentMethod;
import gym_management_backend.gymmanagement.entity.enums.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDto {

    private UUID id;

    private UUID memberId;

    private String memberName;

    private UUID subscriptionId;

    private BigDecimal amount;

    private PaymentMethod paymentMethod;

    private PaymentStatus status;

    private String transactionId;

    private String gatewayOrderId;

    private String gatewayPaymentId;

    private String notes;

    private LocalDateTime paymentDate;

    private UUID gymId;

    private String gymName;
}