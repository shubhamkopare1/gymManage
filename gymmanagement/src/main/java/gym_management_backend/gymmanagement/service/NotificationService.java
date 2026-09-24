package gym_management_backend.gymmanagement.service;


import gym_management_backend.gymmanagement.dto.request.NotificationRequestDto;
import gym_management_backend.gymmanagement.dto.response.NotificationResponseDto;
import gym_management_backend.gymmanagement.entity.enums.NotificationType;

import java.util.List;
import java.util.UUID;

public interface NotificationService {

    NotificationResponseDto createNotification(
            NotificationRequestDto request
    );
    boolean notificationExists(
            UUID memberId,
            UUID gymId,
            NotificationType type,
            String title
    );
    List<NotificationResponseDto> getAllNotifications();

    List<NotificationResponseDto> getMemberNotifications(
            UUID memberId
    );

    NotificationResponseDto getNotificationById(
            UUID notificationId
    );

    NotificationResponseDto markAsRead(
            UUID notificationId
    );

    void markAllAsRead(
            UUID memberId
    );

    long getUnreadCount(
            UUID memberId
    );

    void deleteNotification(
            UUID notificationId
    );
    boolean notificationExists(
            UUID memberId,
            UUID gymId,
            NotificationType type,
            String referenceType,
            UUID referenceId
    );
}