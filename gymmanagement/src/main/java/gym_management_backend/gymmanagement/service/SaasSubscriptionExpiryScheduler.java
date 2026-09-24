package gym_management_backend.gymmanagement.service;

import gym_management_backend.gymmanagement.entity.SaasSubscription;
import gym_management_backend.gymmanagement.entity.enums.SaasSubscriptionStatus;
import gym_management_backend.gymmanagement.repository.SaasSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SaasSubscriptionExpiryScheduler {

    private final SaasSubscriptionRepository subscriptionRepository;

    @Scheduled(cron = "0 30 1 * * *")
    public void expireSaasSubscriptions() {

        LocalDate today = LocalDate.now();

        List<SaasSubscription> subscriptions =
                subscriptionRepository
                        .findAllByStatusAndEndDateBefore(
                                SaasSubscriptionStatus.ACTIVE,
                                today
                        );

        for (SaasSubscription subscription : subscriptions) {

            subscription.setStatus(
                    SaasSubscriptionStatus.EXPIRED
            );

            subscriptionRepository.save(subscription);
        }

        log.info(
                "Expired {} SaaS subscriptions",
                subscriptions.size()
        );
    }
}