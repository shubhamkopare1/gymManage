package gym_management_backend.gymmanagement.repository;



import gym_management_backend.gymmanagement.entity.Gym;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GymRepository
        extends JpaRepository<Gym, UUID> {

    boolean existsByEmail(String email);

    List<Gym> findAllByActiveTrue();

    long countByActiveTrue();

    long countByActiveFalse();
}