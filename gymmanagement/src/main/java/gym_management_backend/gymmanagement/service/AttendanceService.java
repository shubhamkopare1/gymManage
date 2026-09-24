package gym_management_backend.gymmanagement.service;


import gym_management_backend.gymmanagement.dto.request.AttendanceRequestDto;
import gym_management_backend.gymmanagement.dto.request.QrAttendanceRequestDto;
import gym_management_backend.gymmanagement.dto.response.AttendanceResponseDto;
import gym_management_backend.gymmanagement.dto.response.QrCodeResponseDto;

import java.util.List;
import java.util.UUID;

public interface AttendanceService {

    AttendanceResponseDto markAttendance(
            AttendanceRequestDto request
    );

    AttendanceResponseDto markQrAttendance(
            QrAttendanceRequestDto request
    );

    List<AttendanceResponseDto> getAllAttendance();

    List<AttendanceResponseDto> getMemberAttendance(
            UUID memberId
    );

    AttendanceResponseDto getAttendanceById(
            UUID attendanceId
    );

    AttendanceResponseDto updateAttendance(
            UUID attendanceId,
            AttendanceRequestDto request
    );
    QrCodeResponseDto generateMemberQrCode(UUID memberId);

    void deleteAttendance(
            UUID attendanceId
    );
}