package gym_management_backend.gymmanagement.service.impl;

import gym_management_backend.gymmanagement.dto.request.AuditLogRequestDto;
import gym_management_backend.gymmanagement.dto.response.AuditLogResponseDto;
import gym_management_backend.gymmanagement.entity.AuditLog;
import gym_management_backend.gymmanagement.entity.Gym;
import gym_management_backend.gymmanagement.entity.User;
import gym_management_backend.gymmanagement.exception.ResourceNotFoundException;
import gym_management_backend.gymmanagement.mapper.AuditLogMapper;
import gym_management_backend.gymmanagement.repository.AuditLogRepository;
import gym_management_backend.gymmanagement.repository.GymRepository;
import gym_management_backend.gymmanagement.repository.UserRepository;
import gym_management_backend.gymmanagement.security.AuthenticatedUser;
import gym_management_backend.gymmanagement.service.AuditLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl
        implements AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;
    private final GymRepository gymRepository;
    private final HttpServletRequest httpServletRequest;

    @Override
    public AuditLogResponseDto createAuditLog(
            AuditLogRequestDto request
    ) {

        AuthenticatedUser authenticatedUser =
                getAuthenticatedUser();

        User user =
                userRepository
                        .findById(authenticatedUser.getUserId())
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "User not found"
                                )
                        );

        Gym gym = null;

        if (authenticatedUser.getGymId() != null) {

            gym =
                    gymRepository
                            .findById(
                                    authenticatedUser.getGymId()
                            )
                            .orElse(null);
        }

        AuditLog auditLog =
                AuditLog.builder()
                        .user(user)
                        .gym(gym)
                        .action(request.getAction())
                        .entityType(request.getEntityType())
                        .entityId(request.getEntityId())
                        .description(request.getDescription())
                        .ipAddress(
                                getClientIpAddress()
                        )
                        .createdAt(
                                LocalDateTime.now()
                        )
                        .build();

        return AuditLogMapper.toResponse(
                auditLogRepository.save(auditLog)
        );
    }

    @Override
    public List<AuditLogResponseDto> getGymAuditLogs() {

        UUID gymId = getGymId();

        return auditLogRepository
                .findAllByGymIdOrderByCreatedAtDesc(gymId)
                .stream()
                .map(AuditLogMapper::toResponse)
                .toList();
    }

    @Override
    public List<AuditLogResponseDto> getUserAuditLogs(
            UUID userId
    ) {

        return auditLogRepository
                .findAllByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(AuditLogMapper::toResponse)
                .toList();
    }

    private UUID getGymId() {

        AuthenticatedUser user =
                getAuthenticatedUser();

        if (user.getGymId() == null) {
            throw new ResourceNotFoundException(
                    "Gym not found"
            );
        }

        return user.getGymId();
    }

    private AuthenticatedUser getAuthenticatedUser() {

        Object principal =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        if (!(principal instanceof AuthenticatedUser user)) {
            throw new ResourceNotFoundException(
                    "Authenticated user not found"
            );
        }

        return user;
    }

    private String getClientIpAddress() {

        String forwarded =
                httpServletRequest
                        .getHeader("X-Forwarded-For");

        if (forwarded != null &&
                !forwarded.isBlank()) {

            return forwarded.split(",")[0].trim();
        }

        return httpServletRequest.getRemoteAddr();
    }
}
