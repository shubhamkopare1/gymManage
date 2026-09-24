package gym_management_backend.gymmanagement.service;


import gym_management_backend.gymmanagement.dto.request.LoginRequestDto;
import gym_management_backend.gymmanagement.dto.request.RegisterRequestDto;
import gym_management_backend.gymmanagement.dto.response.AuthResponseDto;
import gym_management_backend.gymmanagement.dto.response.UserResponseDto;

public interface AuthService {

    AuthResponseDto register(RegisterRequestDto request);
    UserResponseDto getCurrentUser();
    AuthResponseDto login(LoginRequestDto request);
}
