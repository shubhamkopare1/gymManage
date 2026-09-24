package gym_management_backend.gymmanagement.repository;

import gym_management_backend.gymmanagement.entity.Payment;
import gym_management_backend.gymmanagement.entity.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository
        extends JpaRepository<Payment, UUID> {

    List<Payment> findAllByGymId(UUID gymId);

    List<Payment> findAllByMemberIdAndGymId(
            UUID memberId,
            UUID gymId
    );

    long countByStatus(PaymentStatus status);
    Optional<Payment> findByIdAndGymId(
            UUID paymentId,
            UUID gymId
    );
    Optional<Payment> findByGatewayOrderId(
            String gatewayOrderId
    );

}