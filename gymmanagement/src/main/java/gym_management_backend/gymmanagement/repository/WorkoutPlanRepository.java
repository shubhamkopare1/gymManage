package gym_management_backend.gymmanagement.repository;

import gym_management_backend.gymmanagement.entity.WorkoutPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkoutPlanRepository
        extends JpaRepository<WorkoutPlan, UUID> {

    List<WorkoutPlan> findAllByGymId(UUID gymId);

    List<WorkoutPlan> findAllByMemberIdAndGymId(
            UUID memberId,
            UUID gymId
    );
    List<WorkoutPlan> findAllByTrainerIdAndGymId(
            UUID trainerId,
            UUID gymId
    );
    Optional<WorkoutPlan> findByIdAndGymId(
            UUID planId,
            UUID gymId
    );
}