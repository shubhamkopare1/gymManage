package gym_management_backend.gymmanagement.mapper;


import gym_management_backend.gymmanagement.dto.response.SaasSubscriptionResponseDto;
import gym_management_backend.gymmanagement.entity.SaasSubscription;

public class SaasSubscriptionMapper {

    private SaasSubscriptionMapper() {
    }

    public static SaasSubscriptionResponseDto toResponse(
            SaasSubscription subscription
    ) {

        return SaasSubscriptionResponseDto.builder()
                .id(subscription.getId())

                .gymId(subscription.getGym().getId())
                .gymName(subscription.getGym().getName())

                .saasPlanId(subscription.getSaasPlan().getId())
                .planType(subscription.getSaasPlan().getPlanType())
                .planName(subscription.getSaasPlan().getName())

                .amount(subscription.getAmount())

                .startDate(subscription.getStartDate())
                .endDate(subscription.getEndDate())

                .status(subscription.getStatus())

                .paymentId(subscription.getPaymentId())
                .paymentMethod(subscription.getPaymentMethod())

                .build();
    }
}
