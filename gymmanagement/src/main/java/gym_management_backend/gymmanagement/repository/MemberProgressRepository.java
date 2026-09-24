package gym_management_backend.gymmanagement.repository;

import gym_management_backend.gymmanagement.entity.MemberProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberProgressRepository
        extends JpaRepository<MemberProgress, UUID> {

    List<MemberProgress> findAllByGymIdOrderByProgressDateDesc(
            UUID gymId
    );

    List<MemberProgress>
    findAllByMemberIdAndGymIdOrderByProgressDateDesc(
            UUID memberId,
            UUID gymId
    );

    Optional<MemberProgress> findByIdAndGymId(
            UUID progressId,
            UUID gymId
    );
}