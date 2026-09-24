package gym_management_backend.gymmanagement.repository;

import gym_management_backend.gymmanagement.entity.DietPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DietPlanRepository
        extends JpaRepository<DietPlan, UUID> {

    List<DietPlan> findAllByGymId(UUID gymId);

    List<DietPlan> findAllByMemberIdAndGymId(
            UUID memberId,
            UUID gymId
    );
    List<DietPlan> findAllByTrainerIdAndGymId(
            UUID trainerId,
            UUID gymId
    );
    Optional<DietPlan> findByIdAndGymId(
            UUID planId,
            UUID gymId
    );
}