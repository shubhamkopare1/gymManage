package gym_management_backend.gymmanagement.controller;

import gym_management_backend.gymmanagement.entity.Attendance;
import gym_management_backend.gymmanagement.entity.Member;
import gym_management_backend.gymmanagement.repository.AttendanceRepository;
import gym_management_backend.gymmanagement.repository.MemberRepository;
import gym_management_backend.gymmanagement.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/member/attendance")
@RequiredArgsConstructor
public class MemberAttendanceController {

    private final MemberRepository memberRepository;
    private final AttendanceRepository attendanceRepository;

    @GetMapping
    public List<Attendance> getMyAttendance() {

        AuthenticatedUser user = getUser();

        Member member = memberRepository
                .findByUserId(user.getUserId())
                .orElseThrow(() ->
                        new RuntimeException("Member not found"));

        return attendanceRepository
                .findAllByMemberIdAndGymIdOrderByAttendanceDateDesc(
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