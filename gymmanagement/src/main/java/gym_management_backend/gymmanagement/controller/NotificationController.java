package gym_management_backend.gymmanagement.controller;

import gym_management_backend.gymmanagement.dto.request.NotificationRequestDto;
import gym_management_backend.gymmanagement.dto.response.NotificationResponseDto;
import gym_management_backend.gymmanagement.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/gym/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<NotificationResponseDto>
    createNotification(
            @Valid @RequestBody NotificationRequestDto request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        notificationService
                                .createNotification(request)
                );
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponseDto>>
    getAllNotifications() {

        return ResponseEntity.ok(
                notificationService.getAllNotifications()
        );
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<NotificationResponseDto>>
    getMemberNotifications(
            @PathVariable UUID memberId
    ) {

        return ResponseEntity.ok(
                notificationService
                        .getMemberNotifications(memberId)
        );
    }

    @GetMapping("/{notificationId}")
    public ResponseEntity<NotificationResponseDto>
    getNotificationById(
            @PathVariable UUID notificationId
    ) {

        return ResponseEntity.ok(
                notificationService
                        .getNotificationById(notificationId)
        );
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<NotificationResponseDto>
    markAsRead(
            @PathVariable UUID notificationId
    ) {

        return ResponseEntity.ok(
                notificationService
                        .markAsRead(notificationId)
        );
    }

    @PutMapping("/member/{memberId}/read-all")
    public ResponseEntity<Void>
    markAllAsRead(
            @PathVariable UUID memberId
    ) {

        notificationService.markAllAsRead(memberId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/member/{memberId}/unread-count")
    public ResponseEntity<Long>
    getUnreadCount(
            @PathVariable UUID memberId
    ) {

        return ResponseEntity.ok(
                notificationService
                        .getUnreadCount(memberId)
        );
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void>
    deleteNotification(
            @PathVariable UUID notificationId
    ) {

        notificationService.deleteNotification(
                notificationId
        );

        return ResponseEntity.noContent().build();
    }
}