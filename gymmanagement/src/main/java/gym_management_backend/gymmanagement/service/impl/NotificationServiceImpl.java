package gym_management_backend.gymmanagement.service.impl;

import gym_management_backend.gymmanagement.dto.request.NotificationRequestDto;
import gym_management_backend.gymmanagement.dto.response.NotificationResponseDto;
import gym_management_backend.gymmanagement.entity.Gym;
import gym_management_backend.gymmanagement.entity.Member;
import gym_management_backend.gymmanagement.entity.Notification;
import gym_management_backend.gymmanagement.entity.enums.NotificationType;
import gym_management_backend.gymmanagement.exception.ResourceNotFoundException;
import gym_management_backend.gymmanagement.mapper.NotificationMapper;
import gym_management_backend.gymmanagement.repository.GymRepository;
import gym_management_backend.gymmanagement.repository.MemberRepository;
import gym_management_backend.gymmanagement.repository.NotificationRepository;
import gym_management_backend.gymmanagement.security.AuthenticatedUser;
import gym_management_backend.gymmanagement.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl
        implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;
    private final GymRepository gymRepository;

    @Override
    public NotificationResponseDto createNotification(
            NotificationRequestDto request
    ) {

        UUID gymId = getGymId();

        Gym gym = gymRepository.findById(gymId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Gym not found"
                        )
                );

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

        Notification notification = Notification.builder()
                .title(request.getTitle())
                .message(request.getMessage())
                .type(request.getType())
                .read(false)
                .createdAt(LocalDateTime.now())
                .member(member)
                .gym(gym)
                .build();

        Notification savedNotification =
                notificationRepository.save(notification);

        return NotificationMapper.toResponse(
                savedNotification
        );
    }

    @Override
    public List<NotificationResponseDto> getAllNotifications() {

        UUID gymId = getGymId();

        return notificationRepository
                .findAllByGymIdOrderByCreatedAtDesc(gymId)
                .stream()
                .map(NotificationMapper::toResponse)
                .toList();
    }
    @Override
    public boolean notificationExists(
            UUID memberId,
            UUID gymId,
            NotificationType type,
            String title
    ) {
        return notificationRepository
                .existsByMemberIdAndGymIdAndTypeAndTitle(
                        memberId,
                        gymId,
                        type,
                        title
                );
    }
    @Override
    public List<NotificationResponseDto> getMemberNotifications(
            UUID memberId
    ) {

        UUID gymId = getGymId();

        // Make sure member belongs to current gym
        memberRepository.findByIdAndGymId(
                        memberId,
                        gymId
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Member not found"
                        )
                );

        return notificationRepository
                .findAllByMemberIdAndGymIdOrderByCreatedAtDesc(
                        memberId,
                        gymId
                )
                .stream()
                .map(NotificationMapper::toResponse)
                .toList();
    }

    @Override
    public NotificationResponseDto getNotificationById(
            UUID notificationId
    ) {

        UUID gymId = getGymId();

        Notification notification =
                notificationRepository
                        .findByIdAndGymId(
                                notificationId,
                                gymId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Notification not found"
                                )
                        );

        return NotificationMapper.toResponse(
                notification
        );
    }

    @Override
    public NotificationResponseDto markAsRead(
            UUID notificationId
    ) {

        UUID gymId = getGymId();

        Notification notification =
                notificationRepository
                        .findByIdAndGymId(
                                notificationId,
                                gymId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Notification not found"
                                )
                        );

        notification.setRead(true);

        Notification updated =
                notificationRepository.save(notification);

        return NotificationMapper.toResponse(updated);
    }

    @Override
    public void markAllAsRead(UUID memberId) {

        UUID gymId = getGymId();

        List<Notification> notifications =
                notificationRepository
                        .findAllByMemberIdAndGymIdOrderByCreatedAtDesc(
                                memberId,
                                gymId
                        );

        notifications.forEach(notification ->
                notification.setRead(true)
        );

        notificationRepository.saveAll(notifications);
    }

    @Override
    public long getUnreadCount(UUID memberId) {

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

        return notificationRepository
                .countByMemberIdAndGymIdAndReadFalse(
                        memberId,
                        gymId
                );
    }

    @Override
    public void deleteNotification(UUID notificationId) {

        UUID gymId = getGymId();

        Notification notification =
                notificationRepository
                        .findByIdAndGymId(
                                notificationId,
                                gymId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Notification not found"
                                )
                        );

        notificationRepository.delete(notification);
    }

    private UUID getGymId() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        AuthenticatedUser authenticatedUser =
                (AuthenticatedUser) authentication.getPrincipal();

        UUID gymId = authenticatedUser.getGymId();

        if (gymId == null) {
            throw new ResourceNotFoundException(
                    "User is not associated with any gym"
            );
        }

        return gymId;
    }
    @Override
    public boolean notificationExists(
            UUID memberId,
            UUID gymId,
            NotificationType type,
            String referenceType,
            UUID referenceId
    ) {

        return notificationRepository
                .existsByMemberIdAndGymIdAndTypeAndReferenceTypeAndReferenceId(
                        memberId,
                        gymId,
                        type,
                        referenceType,
                        referenceId
                );
    }
}
