package gym_management_backend.gymmanagement.mapper;


import gym_management_backend.gymmanagement.dto.response.ProgressResponseDto;
import gym_management_backend.gymmanagement.entity.MemberProgress;

public class MemberProgressMapper {

    private MemberProgressMapper() {
    }

    public static ProgressResponseDto toResponse(
            MemberProgress progress
    ) {

        return ProgressResponseDto.builder()
                .id(progress.getId())

                .memberId(
                        progress.getMember().getId()
                )

                .memberName(
                        progress.getMember().getName()
                )

                .progressDate(
                        progress.getProgressDate()
                )

                .weight(
                        progress.getWeight()
                )

                .height(
                        progress.getHeight()
                )

                .bodyFat(
                        progress.getBodyFat()
                )

                .chest(
                        progress.getChest()
                )

                .waist(
                        progress.getWaist()
                )

                .arms(
                        progress.getArms()
                )

                .thighs(
                        progress.getThighs()
                )

                .notes(
                        progress.getNotes()
                )

                .gymId(
                        progress.getGym().getId()
                )

                .gymName(
                        progress.getGym().getName()
                )

                .build();
    }
}