package gym_management_backend.gymmanagement.mapper;


import gym_management_backend.gymmanagement.dto.response.AttendanceResponseDto;
import gym_management_backend.gymmanagement.entity.Attendance;

public class AttendanceMapper {

    private AttendanceMapper() {
    }

    public static AttendanceResponseDto toResponse(
            Attendance attendance
    ) {

        return AttendanceResponseDto.builder()
                .id(attendance.getId())

                .memberId(
                        attendance.getMember().getId()
                )

                .memberName(
                        attendance.getMember().getName()
                )

                .attendanceDate(
                        attendance.getAttendanceDate()
                )

                .checkInTime(
                        attendance.getCheckInTime()
                )

                .status(
                        attendance.getStatus()
                )

                .method(
                        attendance.getMethod()
                )

                .gymId(
                        attendance.getGym().getId()
                )

                .gymName(
                        attendance.getGym().getName()
                )

                .build();
    }
}