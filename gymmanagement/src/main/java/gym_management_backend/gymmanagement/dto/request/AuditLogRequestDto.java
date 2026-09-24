package gym_management_backend.gymmanagement.dto.request;

import gym_management_backend.gymmanagement.entity.AuditAction;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLogRequestDto {

    @NotNull(message = "Action is required")
    private AuditAction action;

    @NotBlank(message = "Entity type is required")
    private String entityType;

    private String entityId;

    @NotBlank(message = "Description is required")
    private String description;
}
