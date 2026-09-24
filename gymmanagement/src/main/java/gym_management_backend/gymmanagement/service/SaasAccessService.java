package gym_management_backend.gymmanagement.service;

import gym_management_backend.gymmanagement.entity.enums.SaasSubscriptionStatus;
import gym_management_backend.gymmanagement.repository.SaasSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SaasAccessService {

    private final SaasSubscriptionRepository saasSubscriptionRepository;

    public boolean hasActiveSubscription(UUID gymId) {

        if (gymId == null) {
            return false;
        }

        return saasSubscriptionRepository
                .existsByGymIdAndStatus(
                        gymId,
                        SaasSubscriptionStatus.ACTIVE
                );
    }
}
