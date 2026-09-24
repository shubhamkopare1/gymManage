package gym_management_backend.gymmanagement.dto.request;

import gym_management_backend.gymmanagement.entity.enums.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationRequestDto {

    @NotNull(message = "Member ID is required")
    private UUID memberId;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Message is required")
    private String message;
    private String referenceType;

    private UUID referenceId;
    @NotNull(message = "Notification type is required")
    private NotificationType type;
}