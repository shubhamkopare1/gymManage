package gym_management_backend.gymmanagement.controller;

import gym_management_backend.gymmanagement.dto.response.AuditLogResponseDto;
import gym_management_backend.gymmanagement.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gym/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping
    public ResponseEntity<List<AuditLogResponseDto>>
    getGymAuditLogs() {

        return ResponseEntity.ok(
                auditLogService.getGymAuditLogs()
        );
    }
}
