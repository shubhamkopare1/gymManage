package gym_management_backend.gymmanagement.repository;


import gym_management_backend.gymmanagement.entity.User;
import gym_management_backend.gymmanagement.entity.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    long countByRole(Role role);

    List<User> findAllByGymId(UUID gymId);

    Optional<User> findByIdAndGymId(UUID userId, UUID gymId);

    List<User> findAllByGymIdAndRole(UUID gymId, Role role);
}