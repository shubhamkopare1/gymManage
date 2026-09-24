package gym_management_backend.gymmanagement.controller;

import gym_management_backend.gymmanagement.entity.Member;
import gym_management_backend.gymmanagement.entity.Trainer;
import gym_management_backend.gymmanagement.repository.MemberRepository;
import gym_management_backend.gymmanagement.repository.TrainerRepository;
import gym_management_backend.gymmanagement.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/trainer/members")
@RequiredArgsConstructor
public class TrainerMemberController {

    private final TrainerRepository trainerRepository;
    private final MemberRepository memberRepository;

    @GetMapping
    public List<Member> getAssignedMembers() {

        AuthenticatedUser user = getUser();

        Trainer trainer = trainerRepository
                .findByUserId(user.getUserId())
                .orElseThrow(() ->
                        new RuntimeException("Trainer not found"));

        return memberRepository
                .findAllByTrainerIdAndGymId(
                        trainer.getId(),
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
    @GetMapping("/{memberId}")
    public Member getMember(
            @PathVariable UUID memberId
    ) {

        AuthenticatedUser user = getUser();

        Trainer trainer = trainerRepository
                .findByUserId(user.getUserId())
                .orElseThrow(() ->
                        new RuntimeException("Trainer not found"));

        Member member = memberRepository
                .findByIdAndGymId(
                        memberId,
                        user.getGymId()
                )
                .orElseThrow(() ->
                        new RuntimeException("Member not found"));

        if (member.getTrainer() == null ||
                !member.getTrainer().getId().equals(trainer.getId())) {

            throw new RuntimeException(
                    "You are not assigned to this member"
            );
        }

        return member;
    }
}