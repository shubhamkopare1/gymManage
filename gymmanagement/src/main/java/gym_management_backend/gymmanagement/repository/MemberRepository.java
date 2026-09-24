package gym_management_backend.gymmanagement.repository;


import gym_management_backend.gymmanagement.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {

    boolean existsByEmailAndGymId(String email, UUID gymId);
    List<Member> findAllByTrainerIdAndGymId(
            UUID trainerId,
            UUID gymId
    );
    List<Member> findAllByGymId(UUID gymId);
    long countByGymId(UUID gymId);
    Optional<Member> findByUserId(UUID userId);
    long countByGymIdAndActiveTrue(UUID gymId);
    Optional<Member> findByIdAndGymId(UUID id, UUID gymId);
}
