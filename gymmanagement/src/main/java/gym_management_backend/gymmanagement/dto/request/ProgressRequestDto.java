package gym_management_backend.gymmanagement.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgressRequestDto {

    @NotNull(message = "Member ID is required")
    private UUID memberId;

    private LocalDate progressDate;

    private BigDecimal weight;

    private BigDecimal height;

    private BigDecimal bodyFat;

    private BigDecimal chest;

    private BigDecimal waist;

    private BigDecimal arms;

    private BigDecimal thighs;

    private String notes;
}