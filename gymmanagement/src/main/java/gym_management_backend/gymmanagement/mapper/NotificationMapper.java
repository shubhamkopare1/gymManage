package gym_management_backend.gymmanagement.mapper;


import gym_management_backend.gymmanagement.dto.response.NotificationResponseDto;
import gym_management_backend.gymmanagement.entity.Notification;

public class NotificationMapper {

    private NotificationMapper() {
    }

    public static NotificationResponseDto toResponse(
            Notification notification
    ) {

        return NotificationResponseDto.builder()
                .id(notification.getId())

                .memberId(
                        notification.getMember() != null
                                ? notification.getMember().getId()
                                : null
                )

                .memberName(
                        notification.getMember() != null
                                ? notification.getMember().getName()
                                : null
                )

                .title(notification.getTitle())
                .message(notification.getMessage())
                .type(notification.getType())
                .read(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .referenceType(notification.getReferenceType())
                .referenceId(notification.getReferenceId())
                .gymId(
                        notification.getGym() != null
                                ? notification.getGym().getId()
                                : null
                )

                .gymName(
                        notification.getGym() != null
                                ? notification.getGym().getName()
                                : null
                )

                .build();
    }
}
