package gym_management_backend.gymmanagement.controller;

import gym_management_backend.gymmanagement.dto.request.CreateAccountRequestDto;
import gym_management_backend.gymmanagement.dto.response.AccountResponseDto;
import gym_management_backend.gymmanagement.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/gym/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/member/{memberId}")
    public AccountResponseDto createMemberAccount(
            @PathVariable UUID memberId,
            @Valid @RequestBody CreateAccountRequestDto request
    ) {
        return accountService.createMemberAccount(
                memberId,
                request
        );
    }

    @PostMapping("/trainer/{trainerId}")
    public AccountResponseDto createTrainerAccount(
            @PathVariable UUID trainerId,
            @Valid @RequestBody CreateAccountRequestDto request
    ) {
        return accountService.createTrainerAccount(
                trainerId,
                request
        );
    }

    @PostMapping("/receptionist/{receptionistId}")
    public AccountResponseDto createReceptionistAccount(
            @PathVariable UUID receptionistId,
            @Valid @RequestBody CreateAccountRequestDto request
    ) {
        return accountService.createReceptionistAccount(
                receptionistId,
                request
        );
    }
}