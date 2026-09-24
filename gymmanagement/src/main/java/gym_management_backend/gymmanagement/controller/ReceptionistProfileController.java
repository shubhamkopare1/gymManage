package gym_management_backend.gymmanagement.controller;

import gym_management_backend.gymmanagement.dto.response.ReceptionistResponseDto;
import gym_management_backend.gymmanagement.entity.Receptionist;
import gym_management_backend.gymmanagement.repository.ReceptionistRepository;
import gym_management_backend.gymmanagement.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reception")
@RequiredArgsConstructor
public class ReceptionistProfileController {

    private final ReceptionistRepository receptionistRepository;

    @GetMapping("/profile")
    public ReceptionistResponseDto getMyProfile() {

        AuthenticatedUser user = getUser();

        Receptionist receptionist =
                receptionistRepository
                        .findByUserId(user.getUserId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Receptionist profile not found"
                                ));

        if (!receptionist.getGym().getId()
                .equals(user.getGymId())) {

            throw new RuntimeException(
                    "Unauthorized gym access"
            );
        }

        return ReceptionistResponseDto.builder()
                .id(receptionist.getId())
                .name(receptionist.getName())
                .email(receptionist.getEmail())
                .phone(receptionist.getPhone())
                .address(receptionist.getAddress())
                .joiningDate(receptionist.getJoiningDate())
                .active(receptionist.isActive())
                .gymId(receptionist.getGym().getId())
                .gymName(receptionist.getGym().getName())
                .build();
    }

    private AuthenticatedUser getUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        return (AuthenticatedUser)
                authentication.getPrincipal();
    }
}