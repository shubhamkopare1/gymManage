package gym_management_backend.gymmanagement.dto.response;


import gym_management_backend.gymmanagement.entity.enums.AttendanceStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceResponseDto {

    private UUID id;

    private UUID memberId;
    private String memberName;

    private LocalDate attendanceDate;

    private LocalDateTime checkInTime;

    private AttendanceStatus status;

    private String method;

    private UUID gymId;
    private String gymName;
}