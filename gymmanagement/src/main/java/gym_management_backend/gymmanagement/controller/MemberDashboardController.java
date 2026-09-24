package gym_management_backend.gymmanagement.controller;

import gym_management_backend.gymmanagement.dto.response.MemberResponseDto;
import gym_management_backend.gymmanagement.entity.Member;
import gym_management_backend.gymmanagement.repository.MemberRepository;
import gym_management_backend.gymmanagement.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberDashboardController {

    private final MemberRepository memberRepository;

    private UUID getUserId() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        AuthenticatedUser user =
                (AuthenticatedUser) authentication.getPrincipal();

        return user.getUserId();
    }

    @GetMapping("/profile")
    public MemberResponseDto getProfile() {

        Member member = memberRepository
                .findByUserId(getUserId())
                .orElseThrow(() ->
                        new RuntimeException("Member profile not found"));

        return MemberResponseDto.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .phone(member.getPhone())
                .gender(member.getGender())
                .dateOfBirth(member.getDateOfBirth())
                .address(member.getAddress())
                .emergencyContact(member.getEmergencyContact())
                .joiningDate(member.getJoiningDate())
                .active(member.isActive())
                .gymId(member.getGym().getId())
                .gymName(member.getGym().getName())
                .build();
    }
}
