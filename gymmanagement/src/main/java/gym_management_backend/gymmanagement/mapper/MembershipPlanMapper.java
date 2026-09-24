package gym_management_backend.gymmanagement.mapper;


import gym_management_backend.gymmanagement.dto.request.MembershipPlanRequestDto;
import gym_management_backend.gymmanagement.dto.response.MembershipPlanResponseDto;
import gym_management_backend.gymmanagement.entity.MembershipPlan;

public class MembershipPlanMapper {

    private MembershipPlanMapper() {
    }

    public static MembershipPlanResponseDto toResponse(
            MembershipPlan plan
    ) {

        return MembershipPlanResponseDto.builder()
                .id(plan.getId())
                .name(plan.getName())
                .description(plan.getDescription())
                .price(plan.getPrice())
                .durationInDays(
                        plan.getDurationInDays()
                )
                .active(plan.isActive())
                .gymId(
                        plan.getGym() != null
                                ? plan.getGym().getId()
                                : null
                )
                .gymName(
                        plan.getGym() != null
                                ? plan.getGym().getName()
                                : null
                )
                .build();
    }

    public static void updateEntity(
            MembershipPlan plan,
            MembershipPlanRequestDto request
    ) {

        plan.setName(request.getName());
        plan.setDescription(
                request.getDescription()
        );
        plan.setPrice(request.getPrice());
        plan.setDurationInDays(
                request.getDurationInDays()
        );
    }
}
