package gym_management_backend.gymmanagement.controller;

import gym_management_backend.gymmanagement.entity.Member;
import gym_management_backend.gymmanagement.repository.MemberRepository;
import gym_management_backend.gymmanagement.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reception/members")
@RequiredArgsConstructor
public class ReceptionistMemberController {

    private final MemberRepository memberRepository;

    @GetMapping
    public List<Member> getMembers() {

        AuthenticatedUser user = getUser();

        return memberRepository
                .findAllByGymId(user.getGymId());
    }

    @GetMapping("/{memberId}")
    public Member getMember(
            @PathVariable java.util.UUID memberId
    ) {

        AuthenticatedUser user = getUser();

        return memberRepository
                .findByIdAndGymId(
                        memberId,
                        user.getGymId()
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Member not found"
                        ));
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
