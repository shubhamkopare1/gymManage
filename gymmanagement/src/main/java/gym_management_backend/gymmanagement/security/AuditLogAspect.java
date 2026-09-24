package gym_management_backend.gymmanagement.security;

import gym_management_backend.gymmanagement.dto.request.AuditLogRequestDto;
import gym_management_backend.gymmanagement.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditLogAspect {

    private final AuditLogService auditLogService;

    @AfterReturning(
            pointcut = "@annotation(auditable)",
            returning = "result"
    )
    public void createAuditLog(
            JoinPoint joinPoint,
            Auditable auditable,
            Object result
    ) {

        try {

            String entityId = extractEntityId(result);

            AuditLogRequestDto request =
                    AuditLogRequestDto.builder()
                            .action(auditable.action())
                            .entityType(auditable.entityType())
                            .entityId(entityId)
                            .description(
                                    auditable.action()
                                            + " "
                                            + auditable.entityType()
                            )
                            .build();

            auditLogService.createAuditLog(request);

        } catch (Exception exception) {

            log.error(
                    "Failed to create audit log",
                    exception
            );
        }
    }

    private String extractEntityId(Object result) {

        if (result == null) {
            return null;
        }

        try {

            var method =
                    result.getClass()
                            .getMethod("getId");

            Object id =
                    method.invoke(result);

            if (id instanceof UUID uuid) {
                return uuid.toString();
            }

            return id != null
                    ? id.toString()
                    : null;

        } catch (Exception exception) {

            return null;
        }
    }
}
