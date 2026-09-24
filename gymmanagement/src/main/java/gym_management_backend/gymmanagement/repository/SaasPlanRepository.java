package gym_management_backend.gymmanagement.repository;

import gym_management_backend.gymmanagement.entity.SaasPlan;
import gym_management_backend.gymmanagement.entity.enums.SaasPlanType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SaasPlanRepository
        extends JpaRepository<SaasPlan, UUID> {

    Optional<SaasPlan> findByPlanType(SaasPlanType planType);

    boolean existsByPlanType(SaasPlanType planType);

    List<SaasPlan> findAllByActiveTrue();
}