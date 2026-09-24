package gym_management_backend.gymmanagement.dto.response;

import gym_management_backend.gymmanagement.entity.enums.SubscriptionStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionResponseDto {

    private UUID id;

    private UUID memberId;

    private String memberName;

    private UUID membershipPlanId;

    private String membershipPlanName;

    private BigDecimal amount;

    private LocalDate startDate;

    private LocalDate endDate;

    private SubscriptionStatus status;

    private UUID gymId;

    private String gymName;
}