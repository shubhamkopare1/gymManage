package gym_management_backend.gymmanagement.service;


import gym_management_backend.gymmanagement.dto.request.ReceptionistRequestDto;
import gym_management_backend.gymmanagement.dto.response.ReceptionistResponseDto;

import java.util.List;
import java.util.UUID;

public interface ReceptionistService {

    ReceptionistResponseDto addReceptionist(
            ReceptionistRequestDto request
    );

    List<ReceptionistResponseDto> getAllReceptionists();

    ReceptionistResponseDto getReceptionistById(
            UUID receptionistId
    );

    ReceptionistResponseDto updateReceptionist(
            UUID receptionistId,
            ReceptionistRequestDto request
    );

    void deleteReceptionist(UUID receptionistId);
}
