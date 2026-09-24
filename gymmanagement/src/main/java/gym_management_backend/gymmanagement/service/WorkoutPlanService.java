package gym_management_backend.gymmanagement.service;


import gym_management_backend.gymmanagement.dto.request.WorkoutPlanRequestDto;
import gym_management_backend.gymmanagement.dto.response.WorkoutPlanResponseDto;

import java.util.List;
import java.util.UUID;

public interface WorkoutPlanService {

    WorkoutPlanResponseDto createPlan(
            WorkoutPlanRequestDto request
    );

    List<WorkoutPlanResponseDto> getAllPlans();

    List<WorkoutPlanResponseDto> getMemberPlans(
            UUID memberId
    );

    WorkoutPlanResponseDto getPlanById(
            UUID planId
    );

    void deletePlan(UUID planId);
}
