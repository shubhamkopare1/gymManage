package gym_management_backend.gymmanagement.service;


import gym_management_backend.gymmanagement.dto.request.CreateAccountRequestDto;
import gym_management_backend.gymmanagement.dto.response.AccountResponseDto;

import java.util.UUID;

public interface AccountService {

    AccountResponseDto createMemberAccount(
            UUID memberId,
            CreateAccountRequestDto request
    );

    AccountResponseDto createTrainerAccount(
            UUID trainerId,
            CreateAccountRequestDto request
    );

    AccountResponseDto createReceptionistAccount(
            UUID receptionistId,
            CreateAccountRequestDto request
    );
}