package gym_management_backend.gymmanagement.mapper;


import gym_management_backend.gymmanagement.dto.request.MemberRequestDto;
import gym_management_backend.gymmanagement.dto.response.MemberResponseDto;
import gym_management_backend.gymmanagement.entity.Member;

public class MemberMapper {

    private MemberMapper() {
    }

    public static MemberResponseDto toResponse(Member member) {

        return MemberResponseDto.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .phone(member.getPhone())
                .gender(member.getGender())
                .dateOfBirth(member.getDateOfBirth())
                .address(member.getAddress())
                .emergencyContact(member.getEmergencyContact())
                .joiningDate(member.getJoiningDate())
                .active(member.isActive())
                .gymId(
                        member.getGym() != null
                                ? member.getGym().getId()
                                : null
                )
                .gymName(
                        member.getGym() != null
                                ? member.getGym().getName()
                                : null
                )
                .build();
    }

    public static void updateEntity(
            Member member,
            MemberRequestDto request
    ) {

        member.setName(request.getName());
        member.setEmail(request.getEmail());
        member.setPhone(request.getPhone());
        member.setGender(request.getGender());
        member.setDateOfBirth(request.getDateOfBirth());
        member.setAddress(request.getAddress());
        member.setEmergencyContact(request.getEmergencyContact());
        member.setJoiningDate(request.getJoiningDate());
    }
}
