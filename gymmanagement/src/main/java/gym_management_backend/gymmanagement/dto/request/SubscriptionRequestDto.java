package gym_management_backend.gymmanagement.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionRequestDto {

    @NotNull(message = "Member ID is required")
    private UUID memberId;

    @NotNull(message = "Membership plan ID is required")
    private UUID membershipPlanId;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;
}