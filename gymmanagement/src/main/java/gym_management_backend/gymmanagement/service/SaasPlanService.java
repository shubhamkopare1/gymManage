package gym_management_backend.gymmanagement.service;


import gym_management_backend.gymmanagement.dto.request.SaasPlanRequestDto;
import gym_management_backend.gymmanagement.dto.response.SaasPlanResponseDto;

import java.util.List;
import java.util.UUID;

public interface SaasPlanService {

    SaasPlanResponseDto createPlan(
            SaasPlanRequestDto request
    );
    List<SaasPlanResponseDto> getActivePlans();
    List<SaasPlanResponseDto> getAllPlans();

    SaasPlanResponseDto getPlanById(
            UUID planId
    );

    SaasPlanResponseDto updatePlan(
            UUID planId,
            SaasPlanRequestDto request
    );

    void deletePlan(
            UUID planId
    );
}