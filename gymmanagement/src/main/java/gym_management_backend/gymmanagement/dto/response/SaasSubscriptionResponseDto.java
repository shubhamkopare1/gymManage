package gym_management_backend.gymmanagement.dto.response;

import gym_management_backend.gymmanagement.entity.enums.SaasPlanType;
import gym_management_backend.gymmanagement.entity.enums.SaasSubscriptionStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaasSubscriptionResponseDto {

    private UUID id;

    private UUID gymId;

    private String gymName;

    private UUID saasPlanId;

    private SaasPlanType planType;

    private String planName;

    private BigDecimal amount;

    private LocalDate startDate;

    private LocalDate endDate;

    private SaasSubscriptionStatus status;

    private String paymentId;

    private String paymentMethod;
}