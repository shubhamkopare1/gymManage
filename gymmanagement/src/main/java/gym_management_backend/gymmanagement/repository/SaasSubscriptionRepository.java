package gym_management_backend.gymmanagement.repository;

import gym_management_backend.gymmanagement.entity.SaasSubscription;
import gym_management_backend.gymmanagement.entity.enums.SaasSubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.time.LocalDate;
public interface SaasSubscriptionRepository
        extends JpaRepository<SaasSubscription, UUID> {

    List<SaasSubscription>
    findAllByGymIdOrderByStartDateDesc(UUID gymId);

    Optional<SaasSubscription>
    findByIdAndGymId(UUID id, UUID gymId);

    Optional<SaasSubscription>
    findByGymIdAndStatus(
            UUID gymId,
            SaasSubscriptionStatus status
    );
    long countByStatus(
            SaasSubscriptionStatus status
    );
    boolean existsByGymIdAndStatus(
            UUID gymId,
            SaasSubscriptionStatus status
    );
    List<SaasSubscription> findAllByStatusAndEndDateBefore(
            SaasSubscriptionStatus status,
            LocalDate date
    );
}