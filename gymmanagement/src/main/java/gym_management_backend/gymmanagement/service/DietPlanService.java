package gym_management_backend.gymmanagement.service;


import gym_management_backend.gymmanagement.dto.request.DietPlanRequestDto;
import gym_management_backend.gymmanagement.dto.response.DietPlanResponseDto;

import java.util.List;
import java.util.UUID;

public interface DietPlanService {

    DietPlanResponseDto createPlan(
            DietPlanRequestDto request
    );

    List<DietPlanResponseDto> getAllPlans();

    List<DietPlanResponseDto> getMemberPlans(
            UUID memberId
    );

    DietPlanResponseDto getPlanById(
            UUID planId
    );

    void deletePlan(UUID planId);
}
