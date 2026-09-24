package gym_management_backend.gymmanagement.dto.request;

import gym_management_backend.gymmanagement.entity.enums.PaymentMethod;
import gym_management_backend.gymmanagement.entity.enums.PaymentStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequestDto {

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