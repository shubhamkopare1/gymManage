package gym_management_backend.gymmanagement.mapper;


import gym_management_backend.gymmanagement.dto.response.AuditLogResponseDto;
import gym_management_backend.gymmanagement.entity.AuditLog;

public class AuditLogMapper {

    private AuditLogMapper() {
    }

    public static AuditLogResponseDto toResponse(
            AuditLog auditLog
    ) {

        return AuditLogResponseDto.builder()

                .id(auditLog.getId())

                .userId(
                        auditLog.getUser() != null
                                ? auditLog.getUser().getId()
                                : null
                )

                .userName(
                        auditLog.getUser() != null
                                ? auditLog.getUser().getName()
                                : null
                )

                .userEmail(
                        auditLog.getUser() != null
                                ? auditLog.getUser().getEmail()
                                : null
                )

                .gymId(
                        auditLog.getGym() != null
                                ? auditLog.getGym().getId()
                                : null
                )

                .gymName(
                        auditLog.getGym() != null
                                ? auditLog.getGym().getName()
                                : null
                )

                .action(auditLog.getAction())

                .entityType(auditLog.getEntityType())

                .entityId(auditLog.getEntityId())

                .description(auditLog.getDescription())

                .ipAddress(auditLog.getIpAddress())

                .createdAt(auditLog.getCreatedAt())

                .build();
    }
}