package gym_management_backend.gymmanagement.service;

import gym_management_backend.gymmanagement.dto.request.SubscriptionRequestDto;
import gym_management_backend.gymmanagement.dto.response.SubscriptionResponseDto;

import java.util.List;
import java.util.UUID;

public interface MemberSubscriptionService {

    SubscriptionResponseDto createSubscription(
            SubscriptionRequestDto request
    );

    List<SubscriptionResponseDto> getAllSubscriptions();

    List<SubscriptionResponseDto> getMemberSubscriptions(
            UUID memberId
    );

    SubscriptionResponseDto getSubscriptionById(
            UUID subscriptionId
    );

    void cancelSubscription(
            UUID subscriptionId
    );

    void autoExpireSubscriptions();
}
