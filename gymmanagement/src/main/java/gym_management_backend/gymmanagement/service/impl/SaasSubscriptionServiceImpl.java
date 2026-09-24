package gym_management_backend.gymmanagement.service.impl;

import gym_management_backend.gymmanagement.dto.request.SaasSubscriptionRequestDto;
import gym_management_backend.gymmanagement.dto.response.SaasSubscriptionResponseDto;
import gym_management_backend.gymmanagement.entity.Gym;
import gym_management_backend.gymmanagement.entity.SaasPlan;
import gym_management_backend.gymmanagement.entity.SaasSubscription;
import gym_management_backend.gymmanagement.entity.enums.SaasSubscriptionStatus;
import gym_management_backend.gymmanagement.exception.ResourceNotFoundException;
import gym_management_backend.gymmanagement.mapper.SaasSubscriptionMapper;
import gym_management_backend.gymmanagement.repository.GymRepository;
import gym_management_backend.gymmanagement.repository.SaasPlanRepository;
import gym_management_backend.gymmanagement.repository.SaasSubscriptionRepository;
import gym_management_backend.gymmanagement.security.AuthenticatedUser;
import gym_management_backend.gymmanagement.service.SaasSubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SaasSubscriptionServiceImpl
        implements SaasSubscriptionService {

    private final SaasSubscriptionRepository subscriptionRepository;
    private final SaasPlanRepository planRepository;
    private final GymRepository gymRepository;

    @Override
    public SaasSubscriptionResponseDto subscribe(
            SaasSubscriptionRequestDto request
    ) {

        UUID gymId = getGymId();

        Gym gym = gymRepository.findById(gymId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Gym not found"
                        )
                );

        SaasPlan plan = planRepository
                .findById(request.getSaasPlanId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "SaaS plan not found"
                        )
                );

        if (!plan.isActive()) {
            throw new RuntimeException(
                    "SaaS plan is inactive"
            );
        }

        if (subscriptionRepository
                .existsByGymIdAndStatus(
                        gymId,
                        SaasSubscriptionStatus.ACTIVE
                )) {

            throw new RuntimeException(
                    "Gym already has an active subscription"
            );
        }

        LocalDate startDate =
                request.getStartDate() != null
                        ? request.getStartDate()
                        : LocalDate.now();

        LocalDate endDate =
                startDate.plusDays(
                        plan.getDurationInDays() - 1
                );

        SaasSubscription subscription =
                SaasSubscription.builder()
                        .gym(gym)
                        .saasPlan(plan)
                        .startDate(startDate)
                        .endDate(endDate)
                        .amount(plan.getPrice())
                        .status(
                                SaasSubscriptionStatus.ACTIVE
                        )
                        .paymentId(
                                request.getPaymentId()
                        )
                        .paymentMethod(
                                request.getPaymentMethod()
                        )
                        .build();

        SaasSubscription saved =
                subscriptionRepository.save(
                        subscription
                );

        return SaasSubscriptionMapper.toResponse(
                saved
        );
    }

    @Override
    public SaasSubscriptionResponseDto
    getCurrentSubscription() {

        UUID gymId = getGymId();

        SaasSubscription subscription =
                subscriptionRepository
                        .findByGymIdAndStatus(
                                gymId,
                                SaasSubscriptionStatus.ACTIVE
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "No active SaaS subscription found"
                                )
                        );

        return SaasSubscriptionMapper.toResponse(
                subscription
        );
    }

    @Override
    public List<SaasSubscriptionResponseDto>
    getSubscriptionHistory() {

        UUID gymId = getGymId();

        return subscriptionRepository
                .findAllByGymIdOrderByStartDateDesc(gymId)
                .stream()
                .map(
                        SaasSubscriptionMapper::toResponse
                )
                .toList();
    }

    @Override
    public SaasSubscriptionResponseDto
    getSubscriptionById(
            UUID subscriptionId
    ) {

        UUID gymId = getGymId();

        SaasSubscription subscription =
                subscriptionRepository
                        .findByIdAndGymId(
                                subscriptionId,
                                gymId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Subscription not found"
                                )
                        );

        return SaasSubscriptionMapper.toResponse(
                subscription
        );
    }

    @Override
    public SaasSubscriptionResponseDto
    cancelSubscription(
            UUID subscriptionId
    ) {

        UUID gymId = getGymId();

        SaasSubscription subscription =
                subscriptionRepository
                        .findByIdAndGymId(
                                subscriptionId,
                                gymId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Subscription not found"
                                )
                        );

        subscription.setStatus(
                SaasSubscriptionStatus.CANCELLED
        );

        return SaasSubscriptionMapper.toResponse(
                subscriptionRepository.save(subscription)
        );
    }

    private UUID getGymId() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        AuthenticatedUser authenticatedUser =
                (AuthenticatedUser)
                        authentication.getPrincipal();

        UUID gymId = authenticatedUser.getGymId();

        if (gymId == null) {
            throw new ResourceNotFoundException(
                    "User is not associated with any gym"
            );
        }

        return gymId;
    }
}