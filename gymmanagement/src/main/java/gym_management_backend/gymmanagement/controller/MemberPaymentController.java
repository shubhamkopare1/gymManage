package gym_management_backend.gymmanagement.controller;

import gym_management_backend.gymmanagement.entity.Member;
import gym_management_backend.gymmanagement.entity.Payment;
import gym_management_backend.gymmanagement.repository.MemberRepository;
import gym_management_backend.gymmanagement.repository.PaymentRepository;
import gym_management_backend.gymmanagement.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/member/payments")
@RequiredArgsConstructor
public class MemberPaymentController {

    private final MemberRepository memberRepository;
    private final PaymentRepository paymentRepository;

    @GetMapping
    public List<Payment> getMyPayments() {

        AuthenticatedUser user = getUser();

        Member member = memberRepository
                .findByUserId(user.getUserId())
                .orElseThrow(() ->
                        new RuntimeException("Member not found"));

        return paymentRepository
                .findAllByMemberIdAndGymId(
                        member.getId(),
                        user.getGymId()
                );
    }

    private AuthenticatedUser getUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        return (AuthenticatedUser) authentication.getPrincipal();
    }
}
