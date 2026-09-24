package gym_management_backend.gymmanagement.repository;

import gym_management_backend.gymmanagement.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AuditLogRepository
        extends JpaRepository<AuditLog, UUID> {

    List<AuditLog> findAllByGymIdOrderByCreatedAtDesc(
            UUID gymId
    );

    List<AuditLog> findAllByUserIdOrderByCreatedAtDesc(
            UUID userId
    );
}