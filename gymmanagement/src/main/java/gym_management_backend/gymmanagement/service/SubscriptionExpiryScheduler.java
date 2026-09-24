package gym_management_backend.gymmanagement.service;

import gym_management_backend.gymmanagement.dto.request.NotificationRequestDto;
import gym_management_backend.gymmanagement.entity.MemberSubscription;
import gym_management_backend.gymmanagement.entity.enums.NotificationType;
import gym_management_backend.gymmanagement.entity.enums.SubscriptionStatus;
import gym_management_backend.gymmanagement.repository.MemberSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubscriptionExpiryScheduler {

    private final MemberSubscriptionRepository
            subscriptionRepository;

    private final NotificationService notificationService;


    // =========================================================
    // 1. EXPIRE MEMBERSHIPS
    // Runs every day at 1:00 AM
    // =========================================================

    @Scheduled(cron = "0 0 1 * * *")
    public void expireSubscriptions() {

        LocalDate today = LocalDate.now();

        List<MemberSubscription> expiredSubscriptions =
                subscriptionRepository
                        .findAllByStatusAndEndDateBefore(
                                SubscriptionStatus.ACTIVE,
                                today
                        );

        for (MemberSubscription subscription :
                expiredSubscriptions) {

            // Change subscription status
            subscription.setStatus(
                    SubscriptionStatus.EXPIRED
            );

            subscriptionRepository.save(subscription);


            // Notification title
            String title = "Membership Expired";


            // Check whether notification already exists
            boolean alreadySent =
                    notificationService.notificationExists(
                            subscription
                                    .getMember()
                                    .getId(),

                            subscription
                                    .getGym()
                                    .getId(),

                            NotificationType.MEMBERSHIP_EXPIRY,

                            "MEMBERSHIP_EXPIRY",

                            subscription.getId()
                    );


            // Create notification only if it doesn't exist
            if (!alreadySent) {

                NotificationRequestDto notification =
                        NotificationRequestDto.builder()
                                .memberId(
                                        subscription
                                                .getMember()
                                                .getId()
                                )
                                .title(title)
                                .message(
                                        "Your membership has expired. "
                                                + "Please renew your membership."
                                )
                                .type(
                                        NotificationType
                                                .MEMBERSHIP_EXPIRY
                                )
                                .referenceType(
                                        "MEMBERSHIP_EXPIRY"
                                )
                                .referenceId(
                                        subscription.getId()
                                )
                                .build();

                notificationService.createNotification(
                        notification
                );
            }
        }


        log.info(
                "Expired {} memberships",
                expiredSubscriptions.size()
        );
    }


    // =========================================================
    // 2. EXPIRY REMINDER
    // Runs every day at 9:00 AM
    // =========================================================

    @Scheduled(cron = "0 0 9 * * *")
    public void sendExpiryReminders() {

        LocalDate today = LocalDate.now();

        LocalDate reminderDate =
                today.plusDays(3);


        /*
         * Find active subscriptions whose end date
         * is before reminderDate + 1.
         *
         * Then we specifically check:
         *
         * subscription.endDate == reminderDate
         */
        List<MemberSubscription> subscriptions =
                subscriptionRepository
                        .findAllByStatusAndEndDateBefore(
                                SubscriptionStatus.ACTIVE,
                                reminderDate.plusDays(1)
                        );


        for (MemberSubscription subscription :
                subscriptions) {

            // Only send reminder exactly 3 days before expiry
            if (subscription.getEndDate()
                    .equals(reminderDate)) {


                String title =
                        "Membership Expiring Soon";


                // Check duplicate notification
                boolean alreadySent =
                        notificationService.notificationExists(
                                subscription
                                        .getMember()
                                        .getId(),

                                subscription
                                        .getGym()
                                        .getId(),

                                NotificationType.MEMBERSHIP_EXPIRY,

                                "MEMBERSHIP_EXPIRY_3_DAY",

                                subscription.getId()
                        );


                // Create only if not already sent
                if (!alreadySent) {

                    NotificationRequestDto notification =
                            NotificationRequestDto.builder()
                                    .memberId(
                                            subscription
                                                    .getMember()
                                                    .getId()
                                    )
                                    .title(title)
                                    .message(
                                            "Your membership will expire "
                                                    + "in 3 days. "
                                                    + "Please renew it."
                                    )
                                    .type(
                                            NotificationType
                                                    .MEMBERSHIP_EXPIRY
                                    )
                                    .referenceType(
                                            "MEMBERSHIP_EXPIRY_3_DAY"
                                    )
                                    .referenceId(
                                            subscription.getId()
                                    )
                                    .build();

                    notificationService
                            .createNotification(
                                    notification
                            );
                }
            }
        }


        log.info(
                "Membership expiry reminder job completed."
        );
    }
}