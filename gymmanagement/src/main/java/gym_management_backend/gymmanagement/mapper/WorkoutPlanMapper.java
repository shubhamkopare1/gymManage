package gym_management_backend.gymmanagement.mapper;


import gym_management_backend.gymmanagement.dto.response.WorkoutPlanResponseDto;
import gym_management_backend.gymmanagement.entity.WorkoutPlan;
import org.springframework.stereotype.Component;

@Component
public class WorkoutPlanMapper {

    private WorkoutPlanMapper() {
    }

    public static WorkoutPlanResponseDto toResponse(
            WorkoutPlan plan
    ) {

        return WorkoutPlanResponseDto.builder()
                .id(plan.getId())
                .name(plan.getName())
                .description(plan.getDescription())
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