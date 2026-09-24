package gym_management_backend.gymmanagement.repository;

import gym_management_backend.gymmanagement.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttendanceRepository
        extends JpaRepository<Attendance, UUID> {

    List<Attendance>
    findAllByGymIdOrderByAttendanceDateDescCheckInTimeDesc(
            UUID gymId
    );

    List<Attendance>
    findAllByMemberIdAndGymIdOrderByAttendanceDateDesc(
            UUID memberId,
            UUID gymId
    );

    Optional<Attendance>
    findByIdAndGymId(
            UUID attendanceId,
            UUID gymId
    );

    Optional<Attendance>
    findByMemberIdAndGymIdAndAttendanceDate(
            UUID memberId,
            UUID gymId,
            LocalDate attendanceDate
    );

    boolean existsByMemberIdAndGymIdAndAttendanceDate(
            UUID memberId,
            UUID gymId,
            LocalDate attendanceDate
    );

    long countByGymId(
            UUID gymId
    );

    long countByGymIdAndAttendanceDate(
            UUID gymId,
            LocalDate attendanceDate
    );
}