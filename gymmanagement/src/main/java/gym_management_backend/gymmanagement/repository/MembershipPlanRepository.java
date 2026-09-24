package gym_management_backend.gymmanagement.repository;

import gym_management_backend.gymmanagement.entity.MembershipPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MembershipPlanRepository
        extends JpaRepository<MembershipPlan, UUID> {

    boolean existsByNameAndGymId(
            String name,
            UUID gymId
    );

    List<MembershipPlan> findAllByGymId(
            UUID gymId
    );

    Optional<MembershipPlan> findByIdAndGymId(
            UUID id,
            UUID gymId
    );
}
