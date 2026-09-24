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
public class SaasSubscriptionRequestDto {

    @NotNull(message = "SaaS plan ID is required")
    private UUID saasPlanId;

    private LocalDate startDate;

    private String paymentId;

    private String paymentMethod;
}