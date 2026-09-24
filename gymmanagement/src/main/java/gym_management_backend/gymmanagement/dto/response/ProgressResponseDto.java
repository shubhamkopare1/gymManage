package gym_management_backend.gymmanagement.dto.response;


import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgressResponseDto {

    private UUID id;

    private UUID memberId;

    private String memberName;

    private LocalDate progressDate;

    private BigDecimal weight;

    private BigDecimal height;

    private BigDecimal bodyFat;

    private BigDecimal chest;

    private BigDecimal waist;

    private BigDecimal arms;

    private BigDecimal thighs;

    private String notes;

    private UUID gymId;

    private String gymName;
}
