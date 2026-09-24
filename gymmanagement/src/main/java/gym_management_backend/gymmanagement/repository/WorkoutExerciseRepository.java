package gym_management_backend.gymmanagement.repository;

import gym_management_backend.gymmanagement.entity.WorkoutExercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkoutExerciseRepository
        extends JpaRepository<WorkoutExercise, UUID> {

    List<WorkoutExercise> findAllByWorkoutPlanId(
            UUID workoutPlanId
    );

    Optional<WorkoutExercise> findByIdAndWorkoutPlanId(
            UUID exerciseId,
            UUID workoutPlanId
    );
}
