package gym_management_backend.gymmanagement.controller;

import gym_management_backend.gymmanagement.entity.Attendance;
import gym_management_backend.gymmanagement.repository.AttendanceRepository;
import gym_management_backend.gymmanagement.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reception/attendance")
@RequiredArgsConstructor
public class ReceptionistAttendanceController {

    private final AttendanceRepository attendanceRepository;

    @GetMapping
    public List<Attendance> getAttendance() {

        AuthenticatedUser user = getUser();

        return attendanceRepository
                .findAllByGymIdOrderByAttendanceDateDescCheckInTimeDesc(
                        user.getGymId()
                );
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