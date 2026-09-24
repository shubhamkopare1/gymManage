package gym_management_backend.gymmanagement.service;


import gym_management_backend.gymmanagement.dto.request.GymRequestDto;
import gym_management_backend.gymmanagement.dto.response.GymResponseDto;

public interface GymService {

    GymResponseDto getMyGym();

    GymResponseDto updateMyGym(GymRequestDto request);
}