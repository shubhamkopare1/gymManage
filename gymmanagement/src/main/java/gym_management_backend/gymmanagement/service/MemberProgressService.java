package gym_management_backend.gymmanagement.service;


import java.util.List;
import java.util.UUID;
import gym_management_backend.gymmanagement.dto.request.ProgressRequestDto;
import gym_management_backend.gymmanagement.dto.response.ProgressResponseDto;
public interface MemberProgressService {

    ProgressResponseDto addProgress(
            ProgressRequestDto request
    );

    List<ProgressResponseDto> getAllProgress();

    List<ProgressResponseDto> getMemberProgress(
            UUID memberId
    );

    ProgressResponseDto getProgressById(
            UUID progressId
    );

    ProgressResponseDto updateProgress(
            UUID progressId,
            ProgressRequestDto request
    );

    void deleteProgress(
            UUID progressId
    );
}