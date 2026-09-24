package gym_management_backend.gymmanagement.repository;

import gym_management_backend.gymmanagement.entity.Receptionist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReceptionistRepository
        extends JpaRepository<Receptionist, UUID> {

    boolean existsByEmailAndGymId(
            String email,
            UUID gymId
    );
    long countByGymId(UUID gymId);
    List<Receptionist> findAllByGymId(
            UUID gymId
    );
    Optional<Receptionist> findByUserId(UUID userId);
    Optional<Receptionist> findByIdAndGymId(
            UUID id,
            UUID gymId
    );
}