package gym_management_backend.gymmanagement.mapper;


import gym_management_backend.gymmanagement.dto.request.SaasPlanRequestDto;
import gym_management_backend.gymmanagement.dto.response.SaasPlanResponseDto;
import gym_management_backend.gymmanagement.entity.SaasPlan;

public class SaasPlanMapper {

    private SaasPlanMapper() {
    }

    public static SaasPlanResponseDto toResponse(
            SaasPlan plan
    ) {

        return SaasPlanResponseDto.builder()
                .id(plan.getId())
                .planType(plan.getPlanType())
                .name(plan.getName())
                .description(plan.getDescription())
                .price(plan.getPrice())
                .durationInDays(plan.getDurationInDays())
                .maxMembers(plan.getMaxMembers())
                .maxTrainers(plan.getMaxTrainers())
                .active(plan.isActive())
                .build();
    }

    public static void updateEntity(
            SaasPlan plan,
            SaasPlanRequestDto request
    ) {

        plan.setPlanType(request.getPlanType());
        plan.setName(request.getName());
        plan.setDescription(request.getDescription());
        plan.setPrice(request.getPrice());
        plan.setDurationInDays(request.getDurationInDays());
        plan.setMaxMembers(request.getMaxMembers());
        plan.setMaxTrainers(request.getMaxTrainers());
    }
}