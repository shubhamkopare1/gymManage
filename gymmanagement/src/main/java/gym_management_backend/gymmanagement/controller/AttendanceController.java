package gym_management_backend.gymmanagement.controller;

import gym_management_backend.gymmanagement.dto.request.AttendanceRequestDto;
import gym_management_backend.gymmanagement.dto.request.QrAttendanceRequestDto;
import gym_management_backend.gymmanagement.dto.response.AttendanceResponseDto;
import gym_management_backend.gymmanagement.dto.response.QrCodeResponseDto;
import gym_management_backend.gymmanagement.service.AttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/gym/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping
    public ResponseEntity<AttendanceResponseDto>
    markAttendance(
            @Valid @RequestBody
            AttendanceRequestDto request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        attendanceService
                                .markAttendance(request)
                );
    }

    @PostMapping("/qr")
    public ResponseEntity<AttendanceResponseDto>
    markQrAttendance(
            @Valid @RequestBody
            QrAttendanceRequestDto request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        attendanceService
                                .markQrAttendance(request)
                );
    }

    @GetMapping
    public ResponseEntity<List<AttendanceResponseDto>>
    getAllAttendance() {

        return ResponseEntity.ok(
                attendanceService.getAllAttendance()
        );
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<AttendanceResponseDto>>
    getMemberAttendance(
            @PathVariable UUID memberId
    ) {

        return ResponseEntity.ok(
                attendanceService
                        .getMemberAttendance(memberId)
        );
    }

    @GetMapping("/{attendanceId}")
    public ResponseEntity<AttendanceResponseDto>
    getAttendanceById(
            @PathVariable UUID attendanceId
    ) {

        return ResponseEntity.ok(
                attendanceService
                        .getAttendanceById(attendanceId)
        );
    }
    @GetMapping("/member/{memberId}/qr")
    public ResponseEntity<QrCodeResponseDto> generateMemberQrCode(
            @PathVariable UUID memberId
    ) {

        return ResponseEntity.ok(
                attendanceService.generateMemberQrCode(
                        memberId
                )
        );
    }
    @PutMapping("/{attendanceId}")
    public ResponseEntity<AttendanceResponseDto>
    updateAttendance(
            @PathVariable UUID attendanceId,
            @RequestBody AttendanceRequestDto request
    ) {

        return ResponseEntity.ok(
                attendanceService.updateAttendance(
                        attendanceId,
                        request
                )
        );
    }

    @DeleteMapping("/{attendanceId}")
    public ResponseEntity<Void>
    deleteAttendance(
            @PathVariable UUID attendanceId
    ) {

        attendanceService.deleteAttendance(
                attendanceId
        );

        return ResponseEntity.noContent().build();
    }
}