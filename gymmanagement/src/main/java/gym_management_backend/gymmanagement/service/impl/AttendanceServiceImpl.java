package gym_management_backend.gymmanagement.service.impl;

import gym_management_backend.gymmanagement.dto.request.AttendanceRequestDto;
import gym_management_backend.gymmanagement.dto.request.QrAttendanceRequestDto;
import gym_management_backend.gymmanagement.dto.response.AttendanceResponseDto;
import gym_management_backend.gymmanagement.dto.response.QrCodeResponseDto;
import gym_management_backend.gymmanagement.entity.Attendance;
import gym_management_backend.gymmanagement.entity.AuditAction;
import gym_management_backend.gymmanagement.entity.Gym;
import gym_management_backend.gymmanagement.entity.Member;
import gym_management_backend.gymmanagement.entity.enums.AttendanceStatus;
import gym_management_backend.gymmanagement.exception.ResourceNotFoundException;
import gym_management_backend.gymmanagement.mapper.AttendanceMapper;
import gym_management_backend.gymmanagement.repository.AttendanceRepository;
import gym_management_backend.gymmanagement.repository.GymRepository;
import gym_management_backend.gymmanagement.repository.MemberRepository;
import gym_management_backend.gymmanagement.security.Auditable;
import gym_management_backend.gymmanagement.security.AuthenticatedUser;
import gym_management_backend.gymmanagement.security.JwtService;
import gym_management_backend.gymmanagement.service.AttendanceService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl
        implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final MemberRepository memberRepository;
    private final GymRepository gymRepository;
    private final JwtService jwtService;
    @Override
    @Auditable(
            action = AuditAction.ATTENDANCE,
            entityType = "ATTENDANCE"
    )
    public AttendanceResponseDto markAttendance(
            AttendanceRequestDto request
    ) {

        UUID gymId = getGymId();

        Member member = memberRepository
                .findByIdAndGymId(
                        request.getMemberId(),
                        gymId
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Member not found"
                        )
                );

        Gym gym = gymRepository.findById(gymId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Gym not found"
                        )
                );

        LocalDate attendanceDate =
                request.getAttendanceDate() != null
                        ? request.getAttendanceDate()
                        : LocalDate.now();

        boolean alreadyMarked =
                attendanceRepository
                        .existsByMemberIdAndGymIdAndAttendanceDate(
                                member.getId(),
                                gymId,
                                attendanceDate
                        );

        if (alreadyMarked) {
            throw new RuntimeException(
                    "Attendance already marked for this date"
            );
        }

        Attendance attendance =
                Attendance.builder()
                        .member(member)
                        .gym(gym)
                        .attendanceDate(attendanceDate)
                        .checkInTime(LocalDateTime.now())
                        .status(
                                request.getStatus() != null
                                        ? request.getStatus()
                                        : AttendanceStatus.PRESENT
                        )
                        .method(
                                request.getMethod() != null
                                        ? request.getMethod()
                                        : "MANUAL"
                        )
                        .build();

        Attendance saved =
                attendanceRepository.save(attendance);

        return AttendanceMapper.toResponse(saved);
    }

    @Override
    @Auditable(
            action = AuditAction.ATTENDANCE,
            entityType = "QR_ATTENDANCE"
    )
    public AttendanceResponseDto markQrAttendance(
            QrAttendanceRequestDto request
    ) {

        UUID gymId = getGymId();

        Claims claims;

        try {

            claims =
                    jwtService.validateMemberQrToken(
                            request.getQrCode()
                    );

        } catch (Exception exception) {

            throw new IllegalArgumentException(
                    "Invalid or expired QR code"
            );
        }

        UUID memberId =
                UUID.fromString(
                        claims.getSubject()
                );

        UUID qrGymId =
                UUID.fromString(
                        claims.get("gymId", String.class)
                );

        if (!gymId.equals(qrGymId)) {

            throw new IllegalArgumentException(
                    "QR code does not belong to this gym"
            );
        }

        Member member =
                memberRepository
                        .findByIdAndGymId(
                                memberId,
                                gymId
                        )
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Member not found"
                                )
                        );

        LocalDate today = LocalDate.now();

        if (attendanceRepository
                .existsByMemberIdAndGymIdAndAttendanceDate(
                        memberId,
                        gymId,
                        today
                )) {

            throw new IllegalArgumentException(
                    "Attendance already marked for today"
            );
        }

        Attendance attendance =
                Attendance.builder()
                        .member(member)
                        .gym(member.getGym())
                        .attendanceDate(today)
                        .checkInTime(LocalDateTime.now())
                        .status(AttendanceStatus.PRESENT)
                        .method("QR")
                        .build();

        return AttendanceMapper.toResponse(
                attendanceRepository.save(attendance)
        );
    }
    @Override
    public List<AttendanceResponseDto>
    getAllAttendance() {

        UUID gymId = getGymId();

        return attendanceRepository
                .findAllByGymIdOrderByAttendanceDateDescCheckInTimeDesc(
                        gymId
                )
                .stream()
                .map(AttendanceMapper::toResponse)
                .toList();
    }

    @Override
    public List<AttendanceResponseDto>
    getMemberAttendance(
            UUID memberId
    ) {

        UUID gymId = getGymId();

        memberRepository.findByIdAndGymId(
                        memberId,
                        gymId
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Member not found"
                        )
                );

        return attendanceRepository
                .findAllByMemberIdAndGymIdOrderByAttendanceDateDesc(
                        memberId,
                        gymId
                )
                .stream()
                .map(AttendanceMapper::toResponse)
                .toList();
    }

    @Override
    public AttendanceResponseDto
    getAttendanceById(
            UUID attendanceId
    ) {

        UUID gymId = getGymId();

        Attendance attendance =
                attendanceRepository
                        .findByIdAndGymId(
                                attendanceId,
                                gymId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Attendance not found"
                                )
                        );

        return AttendanceMapper.toResponse(
                attendance
        );
    }

    @Override
    @Auditable(
            action = AuditAction.UPDATE,
            entityType = "ATTENDANCE"
    )
    public AttendanceResponseDto updateAttendance(
            UUID attendanceId,
            AttendanceRequestDto request
    ) {

        UUID gymId = getGymId();

        Attendance attendance =
                attendanceRepository
                        .findByIdAndGymId(
                                attendanceId,
                                gymId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Attendance not found"
                                )
                        );

        if (request.getStatus() != null) {
            attendance.setStatus(
                    request.getStatus()
            );
        }

        if (request.getMethod() != null) {
            attendance.setMethod(
                    request.getMethod()
            );
        }

        Attendance updated =
                attendanceRepository.save(
                        attendance
                );

        return AttendanceMapper.toResponse(
                updated
        );
    }
    @Override
    public QrCodeResponseDto generateMemberQrCode(
            UUID memberId
    ) {

        UUID gymId = getGymId();

        Member member =
                memberRepository
                        .findByIdAndGymId(
                                memberId,
                                gymId
                        )
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Member not found"
                                )
                        );

        String token =
                jwtService.generateMemberQrToken(
                        member.getId(),
                        gymId
                );

        return QrCodeResponseDto.builder()
                .qrCode(token)
                .build();
    }
    @Override
    @Auditable(
            action = AuditAction.DELETE,
            entityType = "ATTENDANCE"
    )
    public void deleteAttendance(
            UUID attendanceId
    ) {

        UUID gymId = getGymId();

        Attendance attendance =
                attendanceRepository
                        .findByIdAndGymId(
                                attendanceId,
                                gymId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Attendance not found"
                                )
                        );

        attendanceRepository.delete(attendance);
    }

    private UUID getGymId() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        AuthenticatedUser authenticatedUser =
                (AuthenticatedUser)
                        authentication.getPrincipal();

        UUID gymId =
                authenticatedUser.getGymId();

        if (gymId == null) {
            throw new ResourceNotFoundException(
                    "User is not associated with any gym"
            );
        }

        return gymId;
    }
}