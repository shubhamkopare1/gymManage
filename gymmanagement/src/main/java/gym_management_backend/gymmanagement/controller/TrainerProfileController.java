package gym_management_backend.gymmanagement.controller;

import gym_management_backend.gymmanagement.dto.response.TrainerResponseDto;
import gym_management_backend.gymmanagement.entity.DietPlan;
import gym_management_backend.gymmanagement.entity.Trainer;
import gym_management_backend.gymmanagement.repository.DietPlanRepository;
import gym_management_backend.gymmanagement.repository.TrainerRepository;
import gym_management_backend.gymmanagement.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trainer")
@RequiredArgsConstructor
public class TrainerProfileController {

    private final TrainerRepository trainerRepository;
   private final DietPlanRepository dietPlanRepository;
    @GetMapping("/profile")
    public TrainerResponseDto getMyProfile() {

        AuthenticatedUser user = getUser();

        Trainer trainer = trainerRepository
                .findByUserId(user.getUserId())
                .orElseThrow(() ->
                        new RuntimeException("Trainer profile not found"));

        if (!trainer.getGym().getId().equals(user.getGymId())) {
            throw new RuntimeException("Unauthorized gym access");
        }

        return TrainerResponseDto.builder()
                .id(trainer.getId())
                .name(trainer.getName())
                .email(trainer.getEmail())
                .phone(trainer.getPhone())
                .specialization(trainer.getSpecialization())
                .experience(trainer.getExperience())
                .joiningDate(trainer.getJoiningDate())
                .address(trainer.getAddress())
                .active(trainer.isActive())
                .gymId(trainer.getGym().getId())
                .gymName(trainer.getGym().getName())
                .build();
    }

    @GetMapping("/diet-plans")
    public List<DietPlan> getMyDietPlans() {

        AuthenticatedUser user = getUser();

        Trainer trainer = trainerRepository
                .findByUserId(user.getUserId())
                .orElseThrow(() ->
                        new RuntimeException("Trainer not found"));

        return dietPlanRepository
                .findAllByTrainerIdAndGymId(
                        trainer.getId(),
                        user.getGymId()
                );
    }


    private AuthenticatedUser getUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        return (AuthenticatedUser) authentication.getPrincipal();
    }
}