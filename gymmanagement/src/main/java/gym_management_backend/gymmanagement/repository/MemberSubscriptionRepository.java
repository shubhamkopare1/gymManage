package gym_management_backend.gymmanagement.repository;

import gym_management_backend.gymmanagement.entity.MemberSubscription;
import gym_management_backend.gymmanagement.entity.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberSubscriptionRepository
        extends JpaRepository<MemberSubscription, UUID> {

    List<MemberSubscription> findAllByGymId(
            UUID gymId
    );
    List<MemberSubscription> findAllByStatusAndEndDateBefore(
            SubscriptionStatus status,
            LocalDate date
    );
    List<MemberSubscription>
    findAllByStatusAndEndDate(
            SubscriptionStatus status,
            LocalDate date
    );
    List<MemberSubscription> findAllByMemberIdAndGymId(
            UUID memberId,
            UUID gymId
    );
    List<MemberSubscription> findAllByStatus(
            SubscriptionStatus status
    );
    Optional<MemberSubscription> findByIdAndGymId(
            UUID id,
            UUID gymId
    );
    @Query("""
    SELECT s
    FROM MemberSubscription s
    WHERE s.status = :status
    AND s.endDate < :today
""")
    List<MemberSubscription> findExpiredSubscriptions(
            @Param("status") SubscriptionStatus status,
            @Param("today") LocalDate today
    );
    boolean existsByMemberIdAndGymIdAndStatus(
            UUID memberId,
            UUID gymId,
            SubscriptionStatus status
    );
    List<MemberSubscription> findAllByEndDateBeforeAndStatus(
            LocalDate date,
            SubscriptionStatus status
    );
}