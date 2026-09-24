package gym_management_backend.gymmanagement.mapper;


import gym_management_backend.gymmanagement.dto.request.GymRequestDto;
import gym_management_backend.gymmanagement.dto.response.GymResponseDto;
import gym_management_backend.gymmanagement.entity.Gym;

public class GymMapper {

    private GymMapper() {
    }

    public static GymResponseDto toResponse(Gym gym) {

        return GymResponseDto.builder()
                .id(gym.getId())
                .name(gym.getName())
                .email(gym.getEmail())
                .phone(gym.getPhone())
                .address(gym.getAddress())
                .city(gym.getCity())
                .state(gym.getState())
                .pincode(gym.getPincode())
                .logoUrl(gym.getLogoUrl())
                .active(gym.isActive())
                .build();
    }

    public static void updateEntity(
            Gym gym,
            GymRequestDto request
    ) {

        gym.setName(request.getName());
        gym.setEmail(request.getEmail());
        gym.setPhone(request.getPhone());
        gym.setAddress(request.getAddress());
        gym.setCity(request.getCity());
        gym.setState(request.getState());
        gym.setPincode(request.getPincode());
        gym.setLogoUrl(request.getLogoUrl());
    }
}
