package gym_management_backend.gymmanagement.service;


import gym_management_backend.gymmanagement.dto.request.AuditLogRequestDto;
import gym_management_backend.gymmanagement.dto.response.AuditLogResponseDto;

import java.util.List;
import java.util.UUID;

public interface AuditLogService {

    AuditLogResponseDto createAuditLog(
            AuditLogRequestDto request
    );

    List<AuditLogResponseDto> getGymAuditLogs();

    List<AuditLogResponseDto> getUserAuditLogs(
            UUID userId
    );
}