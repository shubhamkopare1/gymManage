package gym_management_backend.gymmanagement.controller;

import gym_management_backend.gymmanagement.entity.MemberSubscription;
import gym_management_backend.gymmanagement.repository.MemberSubscriptionRepository;
import gym_management_backend.gymmanagement.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reception/subscriptions")
@RequiredArgsConstructor
public class ReceptionistSubscriptionController {

    private final MemberSubscriptionRepository subscriptionRepository;

    @GetMapping
    public List<MemberSubscription> getSubscriptions() {

        AuthenticatedUser user = getUser();

        return subscriptionRepository
                .findAllByGymId(user.getGymId());
    }

    private AuthenticatedUser getUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        return (AuthenticatedUser)
                authentication.getPrincipal();
    }
}