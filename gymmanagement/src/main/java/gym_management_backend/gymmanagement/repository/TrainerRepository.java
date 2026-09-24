package gym_management_backend.gymmanagement.repository;

import gym_management_backend.gymmanagement.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TrainerRepository
        extends JpaRepository<Trainer, UUID> {

    boolean existsByEmailAndGymId(
            String email,
            UUID gymId
    );
    Optional<Trainer> findByUserId(UUID userId);
    List<Trainer> findAllByGymId(UUID gymId);
    long countByGymId(UUID gymId);
    Optional<Trainer> findByIdAndGymId(
            UUID id,
            UUID gymId
    );
}
