package gym_management_backend.gymmanagement.service;


import gym_management_backend.gymmanagement.dto.request.TrainerRequestDto;
import gym_management_backend.gymmanagement.dto.response.MemberResponseDto;
import gym_management_backend.gymmanagement.dto.response.TrainerResponseDto;

import java.util.List;
import java.util.UUID;

public interface TrainerService {

    TrainerResponseDto addTrainer(
            TrainerRequestDto request
    );

    List<TrainerResponseDto> getAllTrainers();

    TrainerResponseDto getTrainerById(
            UUID trainerId
    );
    List<MemberResponseDto> getMyMembers();
    TrainerResponseDto updateTrainer(
            UUID trainerId,
            TrainerRequestDto request
    );

    void deleteTrainer(UUID trainerId);
}
