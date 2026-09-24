package gym_management_backend.gymmanagement.service;


import gym_management_backend.gymmanagement.dto.request.MemberRequestDto;
import gym_management_backend.gymmanagement.dto.response.MemberResponseDto;

import java.util.List;
import java.util.UUID;

public interface MemberService {

    MemberResponseDto addMember(MemberRequestDto request);

    List<MemberResponseDto> getAllMembers();

    MemberResponseDto getMemberById(UUID memberId);
    MemberResponseDto getMyProfile();
    MemberResponseDto updateMember(
            UUID memberId,
            MemberRequestDto request
    );

    void deleteMember(UUID memberId);
}
