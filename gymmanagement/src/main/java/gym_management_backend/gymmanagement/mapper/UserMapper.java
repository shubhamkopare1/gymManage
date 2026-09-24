package gym_management_backend.gymmanagement.mapper;


import gym_management_backend.gymmanagement.dto.response.UserResponseDto;
import gym_management_backend.gymmanagement.entity.User;

public class UserMapper {

    private UserMapper() {
    }

    public static UserResponseDto toResponse(User user) {

        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .active(user.isActive())
                .gymId(
                        user.getGym() != null
                                ? user.getGym().getId()
                                : null
                )
                .gymName(
                        user.getGym() != null
                                ? user.getGym().getName()
                                : null
                )
                .build();
    }
}