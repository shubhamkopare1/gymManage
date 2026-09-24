package gym_management_backend.gymmanagement.controller;

import gym_management_backend.gymmanagement.entity.Member;
import gym_management_backend.gymmanagement.entity.MemberProgress;
import gym_management_backend.gymmanagement.repository.MemberProgressRepository;
import gym_management_backend.gymmanagement.repository.MemberRepository;
import gym_management_backend.gymmanagement.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/member/progress")
@RequiredArgsConstructor
public class MemberProgresssController {

    private final MemberRepository memberRepository;
    private final MemberProgressRepository progressRepository;

    @GetMapping
    public List<MemberProgress> getMyProgress() {

        AuthenticatedUser user = getUser();

        Member member = memberRepository
                .findByUserId(user.getUserId())
                .orElseThrow(() ->
                        new RuntimeException("Member not found"));

        return progressRepository
                .findAllByMemberIdAndGymIdOrderByProgressDateDesc(
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