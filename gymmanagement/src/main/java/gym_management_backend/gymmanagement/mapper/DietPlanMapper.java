package gym_management_backend.gymmanagement.mapper;


import gym_management_backend.gymmanagement.dto.response.DietPlanResponseDto;
import gym_management_backend.gymmanagement.entity.DietPlan;

public class DietPlanMapper {

    private DietPlanMapper() {
    }

    public static DietPlanResponseDto toResponse(
            DietPlan plan
    ) {

        return DietPlanResponseDto.builder()
                .id(plan.getId())
                .name(plan.getName())
                .description(plan.getDescription())
                .goal(plan.getGoal())
                .startDate(plan.getStartDate())
                .endDate(plan.getEndDate())
                .active(plan.isActive())

                .memberId(
                        plan.getMember().getId()
                )

                .memberName(
                        plan.getMember().getName()
                )

                .trainerId(
                        plan.getTrainer() != null
                                ? plan.getTrainer().getId()
                                : null
                )

                .trainerName(
                        plan.getTrainer() != null
                                ? plan.getTrainer().getName()
                                : null
                )

                .gymId(
                        plan.getGym().getId()
                )

                .gymName(
                        plan.getGym().getName()
                )

                .build();
    }
}