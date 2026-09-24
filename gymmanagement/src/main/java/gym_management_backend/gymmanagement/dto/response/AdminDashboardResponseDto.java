package gym_management_backend.gymmanagement.dto.response;


import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminDashboardResponseDto {

    private long totalGyms;

    private long activeGyms;

    private long inactiveGyms;

    private long totalUsers;

    private long totalMembers;

    private long totalTrainers;

    private long totalReceptionists;

    private long activeSubscriptions;

    private BigDecimal totalRevenue;
}
