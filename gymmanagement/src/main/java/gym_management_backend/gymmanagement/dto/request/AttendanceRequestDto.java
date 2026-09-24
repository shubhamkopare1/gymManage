package gym_management_backend.gymmanagement.dto.request;

import gym_management_backend.gymmanagement.entity.enums.AttendanceStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceRequestDto {

    @NotNull(message = "Member ID is required")
    private UUID memberId;

    private LocalDate attendanceDate;

    private AttendanceStatus status;

    private String method;
}