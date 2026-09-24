package gym_management_backend.gymmanagement.service;


import gym_management_backend.gymmanagement.dto.request.MembershipPlanRequestDto;
import gym_management_backend.gymmanagement.dto.response.MembershipPlanResponseDto;

import java.util.List;
import java.util.UUID;

public interface MembershipPlanService {

    MembershipPlanResponseDto addPlan(
            MembershipPlanRequestDto request
    );

    List<MembershipPlanResponseDto> getAllPlans();

    MembershipPlanResponseDto getPlanById(
            UUID planId
    );

    MembershipPlanResponseDto updatePlan(
            UUID planId,
            MembershipPlanRequestDto request
    );

    void deletePlan(UUID planId);
}