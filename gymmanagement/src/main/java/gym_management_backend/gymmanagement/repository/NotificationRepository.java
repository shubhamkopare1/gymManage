package gym_management_backend.gymmanagement.repository;


import gym_management_backend.gymmanagement.entity.Notification;
import gym_management_backend.gymmanagement.entity.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository
        extends JpaRepository<Notification, UUID> {


    List<Notification> findAllByGymIdOrderByCreatedAtDesc(
            UUID gymId
    );

    List<Notification> findAllByMemberIdAndGymIdOrderByCreatedAtDesc(
            UUID memberId,
            UUID gymId
    );

    java.util.Optional<Notification> findByIdAndGymId(
            UUID notificationId,
            UUID gymId
    );

    java.util.Optional<Notification> findByIdAndMemberIdAndGymId(
            UUID notificationId,
            UUID memberId,
            UUID gymId
    );

    long countByMemberIdAndGymIdAndReadFalse(
            UUID memberId,
            UUID gymId
    );

    // Prevent duplicate notifications
    boolean existsByMemberIdAndGymIdAndTypeAndReferenceTypeAndReferenceId(
            UUID memberId,
            UUID gymId,
            NotificationType type,
            String referenceType,
            UUID referenceId
    );
    boolean existsByMemberIdAndGymIdAndTypeAndTitle(
            UUID memberId,
            UUID gymId,
            NotificationType type,
            String title
    );
}