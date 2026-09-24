package gym_management_backend.gymmanagement.dto.response;

import gym_management_backend.gymmanagement.entity.enums.NotificationType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponseDto {

    private UUID id;

    private UUID memberId;
    private String memberName;

    private String title;
    private String message;

    private NotificationType type;

    private boolean read;
    private String referenceType;

    private UUID referenceId;
    private LocalDateTime createdAt;

    private UUID gymId;
    private String gymName;
}