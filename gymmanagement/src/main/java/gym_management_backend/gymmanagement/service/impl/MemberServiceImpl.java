package gym_management_backend.gymmanagement.service.impl;

import gym_management_backend.gymmanagement.dto.request.MemberRequestDto;
import gym_management_backend.gymmanagement.dto.response.MemberResponseDto;
import gym_management_backend.gymmanagement.entity.AuditAction;
import gym_management_backend.gymmanagement.entity.Gym;
import gym_management_backend.gymmanagement.entity.Member;
import gym_management_backend.gymmanagement.entity.User;
import gym_management_backend.gymmanagement.entity.enums.Role;
import gym_management_backend.gymmanagement.exception.ResourceNotFoundException;
import gym_management_backend.gymmanagement.mapper.MemberMapper;
import gym_management_backend.gymmanagement.repository.GymRepository;
import gym_management_backend.gymmanagement.repository.MemberRepository;
import gym_management_backend.gymmanagement.repository.UserRepository;
import gym_management_backend.gymmanagement.security.Auditable;
import gym_management_backend.gymmanagement.security.AuthenticatedUser;
import gym_management_backend.gymmanagement.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final GymRepository gymRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    @Auditable(
            action = AuditAction.CREATE,
            entityType = "MEMBER"
    )
    public MemberResponseDto addMember(
            MemberRequestDto request
    ) {

        UUID gymId = getGymId();

        if (memberRepository.existsByEmailAndGymId(
                request.getEmail(),
                gymId
        )) {
            throw new RuntimeException(
                    "Member email already exists in this gym"
            );
        }

        Gym gym = gymRepository.findById(gymId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Gym not found"
                        )
                );

        Member member = Member.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .gender(request.getGender())
                .dateOfBirth(request.getDateOfBirth())
                .address(request.getAddress())
                .emergencyContact(request.getEmergencyContact())
                .joiningDate(
                        request.getJoiningDate() != null
                                ? request.getJoiningDate()
                                : LocalDate.now()
                )
                .active(true)
                .gym(gym)
                .build();

        Member savedMember =
                memberRepository.save(member);

        // Create login account
        if (request.getLoginEmail() != null
                && !request.getLoginEmail().isBlank()
                && request.getLoginPassword() != null
                && !request.getLoginPassword().isBlank()) {

            if (userRepository.existsByEmail(
                    request.getLoginEmail()
            )) {
                throw new RuntimeException(
                        "Login email already registered"
                );
            }

            User user = User.builder()
                    .name(savedMember.getName())
                    .email(request.getLoginEmail())
                    .password(
                            passwordEncoder.encode(
                                    request.getLoginPassword()
                            )
                    )
                    .role(Role.MEMBER)
                    .gym(gym)
                    .active(true)
                    .build();

            User savedUser =
                    userRepository.save(user);

            savedMember.setUser(savedUser);

            memberRepository.save(savedMember);
        }

        return MemberMapper.toResponse(savedMember);
    }

    @Override
    public List<MemberResponseDto> getAllMembers() {

        UUID gymId = getGymId();

        return memberRepository.findAllByGymId(gymId)
                .stream()
                .map(MemberMapper::toResponse)
                .toList();
    }

    @Override
    public MemberResponseDto getMemberById(
            UUID memberId
    ) {

        UUID gymId = getGymId();

        Member member = memberRepository
                .findByIdAndGymId(memberId, gymId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Member not found"
                        )
                );

        return MemberMapper.toResponse(member);
    }
    @Override
    public MemberResponseDto getMyProfile() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        AuthenticatedUser authenticatedUser =
                (AuthenticatedUser) authentication.getPrincipal();

        UUID userId = authenticatedUser.getUserId();
        UUID gymId = authenticatedUser.getGymId();

        Member member = memberRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Member profile not found"
                        ));

        if (!member.getGym().getId().equals(gymId)) {
            throw new RuntimeException(
                    "You cannot access another gym"
            );
        }

        return MemberMapper.toResponse(member);
    }


    @Auditable(
            action = AuditAction.UPDATE,
            entityType = "MEMBER"
    )
    @Override
    public MemberResponseDto updateMember(
            UUID memberId,
            MemberRequestDto request
    ) {

        UUID gymId = getGymId();

        Member member = memberRepository
                .findByIdAndGymId(memberId, gymId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Member not found"
                        )
                );

        MemberMapper.updateEntity(member, request);

        Member updatedMember =
                memberRepository.save(member);

        return MemberMapper.toResponse(updatedMember);
    }


    @Auditable(
            action = AuditAction.DELETE,
            entityType = "MEMBER"
    )
    @Override
    public void deleteMember(UUID memberId) {

        UUID gymId = getGymId();

        Member member = memberRepository
                .findByIdAndGymId(memberId, gymId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Member not found"
                        )
                );

        memberRepository.delete(member);
    }

    private UUID getGymId() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        AuthenticatedUser authenticatedUser =
                (AuthenticatedUser)
                        authentication.getPrincipal();

        UUID gymId = authenticatedUser.getGymId();

        if (gymId == null) {
            throw new ResourceNotFoundException(
                    "User is not associated with any gym"
            );
        }

        return gymId;
    }

}