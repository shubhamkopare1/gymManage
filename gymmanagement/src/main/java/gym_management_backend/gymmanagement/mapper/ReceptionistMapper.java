package gym_management_backend.gymmanagement.mapper;


import gym_management_backend.gymmanagement.dto.request.ReceptionistRequestDto;
import gym_management_backend.gymmanagement.dto.response.ReceptionistResponseDto;
import gym_management_backend.gymmanagement.entity.Receptionist;

public class ReceptionistMapper {

    private ReceptionistMapper() {
    }

    public static ReceptionistResponseDto toResponse(
            Receptionist receptionist
    ) {

        return ReceptionistResponseDto.builder()
                .id(receptionist.getId())
                .name(receptionist.getName())
                .email(receptionist.getEmail())
                .phone(receptionist.getPhone())
                .address(receptionist.getAddress())
                .joiningDate(receptionist.getJoiningDate())
                .active(receptionist.isActive())
                .gymId(
                        receptionist.getGym() != null
                                ? receptionist.getGym().getId()
                                : null
                )
                .gymName(
                        receptionist.getGym() != null
                                ? receptionist.getGym().getName()
                                : null
                )
                .build();
    }

    public static void updateEntity(
            Receptionist receptionist,
            ReceptionistRequestDto request
    ) {

        receptionist.setName(request.getName());
        receptionist.setEmail(request.getEmail());
        receptionist.setPhone(request.getPhone());
        receptionist.setAddress(request.getAddress());
        receptionist.setJoiningDate(
                request.getJoiningDate()
        );
    }
}
