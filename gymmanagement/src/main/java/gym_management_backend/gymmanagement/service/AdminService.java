package gym_management_backend.gymmanagement.service;


import gym_management_backend.gymmanagement.dto.response.AdminDashboardResponseDto;
import gym_management_backend.gymmanagement.dto.response.AdminGymResponseDto;
import gym_management_backend.gymmanagement.dto.response.AdminUserResponseDto;

import java.util.List;
import java.util.UUID;

public interface AdminService {

    AdminDashboardResponseDto getDashboard();

    List<AdminGymResponseDto> getAllGyms();

    AdminGymResponseDto getGymById(UUID gymId);

    AdminGymResponseDto activateGym(UUID gymId);

    AdminGymResponseDto deactivateGym(UUID gymId);

    List<AdminUserResponseDto> getAllUsers();

    List<AdminUserResponseDto> getUsersByGym(UUID gymId);
}