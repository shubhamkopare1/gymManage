package gym_management_backend.gymmanagement.dto.response;

import gym_management_backend.gymmanagement.entity.AuditAction;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLogResponseDto {

    private UUID id;

    private UUID userId;
    private String userName;
    private String userEmail;

    private UUID gymId;
    private String gymName;

    private AuditAction action;

    private String entityType;
    private String entityId;

    private String description;

    private String ipAddress;

    private LocalDateTime createdAt;
}
