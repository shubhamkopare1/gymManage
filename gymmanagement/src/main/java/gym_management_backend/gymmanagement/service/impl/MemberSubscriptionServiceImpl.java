package gym_management_backend.gymmanagement.service.impl;

import gym_management_backend.gymmanagement.dto.request.SubscriptionRequestDto;
import gym_management_backend.gymmanagement.dto.response.SubscriptionResponseDto;
import gym_management_backend.gymmanagement.entity.*;
import gym_management_backend.gymmanagement.entity.enums.SubscriptionStatus;
import gym_management_backend.gymmanagement.exception.ResourceNotFoundException;
import gym_management_backend.gymmanagement.mapper.MemberSubscriptionMapper;
import gym_management_backend.gymmanagement.repository.GymRepository;
import gym_management_backend.gymmanagement.repository.MemberRepository;
import gym_management_backend.gymmanagement.repository.MemberSubscriptionRepository;
import gym_management_backend.gymmanagement.repository.MembershipPlanRepository;
import gym_management_backend.gymmanagement.security.Auditable;
import gym_management_backend.gymmanagement.security.AuthenticatedUser;
import gym_management_backend.gymmanagement.service.MemberSubscriptionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberSubscriptionServiceImpl
        implements MemberSubscriptionService {

    private final MemberSubscriptionRepository subscriptionRepository;
    private final MemberRepository memberRepository;
    private final MembershipPlanRepository planRepository;
    private final GymRepository gymRepository;
    @Override
    @Auditable(
            action = AuditAction.CREATE,
            entityType = "MEMBER_SUBSCRIPTION"
    )
    public SubscriptionResponseDto createSubscription(
            SubscriptionRequestDto request
    ) {

        UUID gymId = getGymId();

        if (subscriptionRepository
                .existsByMemberIdAndGymIdAndStatus(
                        request.getMemberId(),
                        gymId,
                        SubscriptionStatus.ACTIVE
                )) {

            throw new IllegalArgumentException(
                    "Member already has an active subscription"
            );
        }

        Member member = memberRepository
                .findByIdAndGymId(
                        request.getMemberId(),
                        gymId
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Member not found"
                        )
                );

        MembershipPlan plan = planRepository
                .findByIdAndGymId(
                        request.getMembershipPlanId(),
                        gymId
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Membership plan not found"
                        )
                );

        if (!plan.isActive()) {
            throw new IllegalArgumentException(
                    "Membership plan is inactive"
            );
        }

        LocalDate startDate = request.getStartDate() != null
                ? request.getStartDate()
                : LocalDate.now();

        LocalDate endDate = startDate.plusDays(
                plan.getDurationInDays() - 1
        );

        Gym gym = gymRepository.findById(gymId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Gym not found"
                        )
                );

        MemberSubscription subscription =
                MemberSubscription.builder()
                        .member(member)
                        .membershipPlan(plan)
                        .startDate(startDate)
                        .endDate(endDate)
                        .amount(plan.getPrice())
                        .status(SubscriptionStatus.ACTIVE)
                        .gym(gym)
                        .build();

        MemberSubscription savedSubscription =
                subscriptionRepository.save(
                        subscription
                );

        return MemberSubscriptionMapper.toResponse(
                savedSubscription
        );
    }
    @Override
    public List<SubscriptionResponseDto>
    getAllSubscriptions() {

        UUID gymId = getGymId();

        return subscriptionRepository
                .findAllByGymId(gymId)
                .stream()
                .map(
                        MemberSubscriptionMapper::toResponse
                )
                .toList();
    }

    @Override
    public List<SubscriptionResponseDto>
    getMemberSubscriptions(
            UUID memberId
    ) {

        UUID gymId = getGymId();

        // Make sure member belongs to this gym
        memberRepository
                .findByIdAndGymId(
                        memberId,
                        gymId
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Member not found"
                        )
                );

        return subscriptionRepository
                .findAllByMemberIdAndGymId(
                        memberId,
                        gymId
                )
                .stream()
                .map(
                        MemberSubscriptionMapper::toResponse
                )
                .toList();
    }

    @Override
    public SubscriptionResponseDto
    getSubscriptionById(
            UUID subscriptionId
    ) {

        UUID gymId = getGymId();

        MemberSubscription subscription =
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

        return MemberSubscriptionMapper.toResponse(
                subscription
        );
    }
    @Override
    @Auditable(
            action = AuditAction.CANCEL,
            entityType = "MEMBER_SUBSCRIPTION"
    )
    public void cancelSubscription(
            UUID subscriptionId
    ) {

        UUID gymId = getGymId();

        MemberSubscription subscription =
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
                SubscriptionStatus.CANCELLED
        );

        subscriptionRepository.save(subscription);
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
    @Override
    @Transactional
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Kolkata")
    public void autoExpireSubscriptions() {

        LocalDate today = LocalDate.now();

        List<MemberSubscription> subscriptions =
                subscriptionRepository.findAllByEndDateBeforeAndStatus(
                        today,
                        SubscriptionStatus.ACTIVE
                );

        for (MemberSubscription subscription : subscriptions) {
            subscription.setStatus(SubscriptionStatus.EXPIRED);
        }

        subscriptionRepository.saveAll(subscriptions);
    }
}