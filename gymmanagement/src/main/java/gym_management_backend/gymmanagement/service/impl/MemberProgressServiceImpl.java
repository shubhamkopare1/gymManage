package gym_management_backend.gymmanagement.service.impl;

import gym_management_backend.gymmanagement.dto.request.ProgressRequestDto;
import gym_management_backend.gymmanagement.dto.response.ProgressResponseDto;
import gym_management_backend.gymmanagement.entity.AuditAction;
import gym_management_backend.gymmanagement.entity.Gym;
import gym_management_backend.gymmanagement.entity.Member;
import gym_management_backend.gymmanagement.entity.MemberProgress;
import gym_management_backend.gymmanagement.exception.ResourceNotFoundException;
import gym_management_backend.gymmanagement.mapper.MemberProgressMapper;
import gym_management_backend.gymmanagement.repository.GymRepository;
import gym_management_backend.gymmanagement.repository.MemberProgressRepository;
import gym_management_backend.gymmanagement.repository.MemberRepository;
import gym_management_backend.gymmanagement.security.Auditable;
import gym_management_backend.gymmanagement.security.AuthenticatedUser;
import gym_management_backend.gymmanagement.service.MemberProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberProgressServiceImpl
        implements MemberProgressService {

    private final MemberProgressRepository progressRepository;

    private final MemberRepository memberRepository;

    private final GymRepository gymRepository;

    @Override
    @Auditable(
            action = AuditAction.CREATE,
            entityType = "MEMBER_PROGRESS"
    )
    public ProgressResponseDto addProgress(
            ProgressRequestDto request
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

        Gym gym = gymRepository
                .findById(gymId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Gym not found"
                        )
                );

        MemberProgress progress =
                MemberProgress.builder()
                        .member(member)

                        .progressDate(
                                request.getProgressDate() != null
                                        ? request.getProgressDate()
                                        : LocalDate.now()
                        )

                        .weight(request.getWeight())
                        .height(request.getHeight())
                        .bodyFat(request.getBodyFat())
                        .chest(request.getChest())
                        .waist(request.getWaist())
                        .arms(request.getArms())
                        .thighs(request.getThighs())
                        .notes(request.getNotes())
                        .gym(gym)
                        .build();

        MemberProgress savedProgress =
                progressRepository.save(progress);

        return MemberProgressMapper.toResponse(
                savedProgress
        );
    }

    @Override
    public List<ProgressResponseDto> getAllProgress() {

        UUID gymId = getGymId();

        return progressRepository
                .findAllByGymIdOrderByProgressDateDesc(
                        gymId
                )
                .stream()
                .map(MemberProgressMapper::toResponse)
                .toList();
    }

    @Override
    public List<ProgressResponseDto> getMemberProgress(
            UUID memberId
    ) {

        UUID gymId = getGymId();

        memberRepository
                .findByIdAndGymId(
                        memberId,
                        gymId
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Member not found"
                        )
                );

        return progressRepository
                .findAllByMemberIdAndGymIdOrderByProgressDateDesc(
                        memberId,
                        gymId
                )
                .stream()
                .map(MemberProgressMapper::toResponse)
                .toList();
    }

    @Override
    public ProgressResponseDto getProgressById(
            UUID progressId
    ) {

        UUID gymId = getGymId();

        MemberProgress progress =
                progressRepository
                        .findByIdAndGymId(
                                progressId,
                                gymId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Progress record not found"
                                )
                        );

        return MemberProgressMapper.toResponse(
                progress
        );
    }

    @Override
    @Auditable(
            action = AuditAction.UPDATE,
            entityType = "MEMBER_PROGRESS"
    )
    public ProgressResponseDto updateProgress(
            UUID progressId,
            ProgressRequestDto request
    ) {

        UUID gymId = getGymId();

        MemberProgress progress =
                progressRepository
                        .findByIdAndGymId(
                                progressId,
                                gymId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Progress record not found"
                                )
                        );

        /*
         * Verify member belongs to current gym
         */
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

        progress.setMember(member);

        progress.setProgressDate(
                request.getProgressDate() != null
                        ? request.getProgressDate()
                        : progress.getProgressDate()
        );

        progress.setWeight(request.getWeight());
        progress.setHeight(request.getHeight());
        progress.setBodyFat(request.getBodyFat());
        progress.setChest(request.getChest());
        progress.setWaist(request.getWaist());
        progress.setArms(request.getArms());
        progress.setThighs(request.getThighs());
        progress.setNotes(request.getNotes());

        MemberProgress updatedProgress =
                progressRepository.save(progress);

        return MemberProgressMapper.toResponse(
                updatedProgress
        );
    }

    @Override
    @Auditable(
            action = AuditAction.DELETE,
            entityType = "MEMBER_PROGRESS"
    )
    public void deleteProgress(
            UUID progressId
    ) {

        UUID gymId = getGymId();

        MemberProgress progress =
                progressRepository
                        .findByIdAndGymId(
                                progressId,
                                gymId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Progress record not found"
                                )
                        );

        progressRepository.delete(progress);
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