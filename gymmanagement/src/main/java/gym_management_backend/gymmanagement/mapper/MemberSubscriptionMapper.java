package gym_management_backend.gymmanagement.mapper;


import gym_management_backend.gymmanagement.dto.response.SubscriptionResponseDto;
import gym_management_backend.gymmanagement.entity.MemberSubscription;

public class MemberSubscriptionMapper {

    private MemberSubscriptionMapper() {
    }

    public static SubscriptionResponseDto toResponse(
            MemberSubscription subscription
    ) {

        return SubscriptionResponseDto.builder()
                .id(subscription.getId())

                .memberId(
                        subscription.getMember().getId()
                )

                .memberName(
                        subscription.getMember().getName()
                )

                .membershipPlanId(
                        subscription
                                .getMembershipPlan()
                                .getId()
                )

                .membershipPlanName(
                        subscription
                                .getMembershipPlan()
                                .getName()
                )

                .amount(
                        subscription.getAmount()
                )

                .startDate(
                        subscription.getStartDate()
                )

                .endDate(
                        subscription.getEndDate()
                )

                .status(
                        subscription.getStatus()
                )

                .gymId(
                        subscription.getGym().getId()
                )

                .gymName(
                        subscription.getGym().getName()
                )

                .build();
    }
}
