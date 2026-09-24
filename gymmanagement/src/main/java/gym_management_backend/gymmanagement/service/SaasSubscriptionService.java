package gym_management_backend.gymmanagement.service;


import gym_management_backend.gymmanagement.dto.request.SaasSubscriptionRequestDto;
import gym_management_backend.gymmanagement.dto.response.SaasSubscriptionResponseDto;

import java.util.List;
import java.util.UUID;

public interface SaasSubscriptionService {

    SaasSubscriptionResponseDto subscribe(
            SaasSubscriptionRequestDto request
    );

    SaasSubscriptionResponseDto getCurrentSubscription();

    List<SaasSubscriptionResponseDto> getSubscriptionHistory();

    SaasSubscriptionResponseDto getSubscriptionById(
            UUID subscriptionId
    );

    SaasSubscriptionResponseDto cancelSubscription(
            UUID subscriptionId
    );
}