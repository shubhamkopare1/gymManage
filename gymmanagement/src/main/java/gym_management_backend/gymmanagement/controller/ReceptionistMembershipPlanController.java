package gym_management_backend.gymmanagement.controller;

import gym_management_backend.gymmanagement.entity.MembershipPlan;
import gym_management_backend.gymmanagement.repository.MembershipPlanRepository;
import gym_management_backend.gymmanagement.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reception/membership-plans")
@RequiredArgsConstructor
public class ReceptionistMembershipPlanController {

    private final MembershipPlanRepository membershipPlanRepository;

    @GetMapping
    public List<MembershipPlan> getPlans() {

        AuthenticatedUser user = getUser();

        return membershipPlanRepository
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
