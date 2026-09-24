package gym_management_backend.gymmanagement.mapper;


import gym_management_backend.gymmanagement.dto.request.TrainerRequestDto;
import gym_management_backend.gymmanagement.dto.response.TrainerResponseDto;
import gym_management_backend.gymmanagement.entity.Trainer;

public class TrainerMapper {

    private TrainerMapper() {
    }

    public static TrainerResponseDto toResponse(
            Trainer trainer
    ) {

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
                .gymId(
                        trainer.getGym() != null
                                ? trainer.getGym().getId()
                                : null
                )
                .gymName(
                        trainer.getGym() != null
                                ? trainer.getGym().getName()
                                : null
                )
                .build();
    }

    public static void updateEntity(
            Trainer trainer,
            TrainerRequestDto request
    ) {

        trainer.setName(request.getName());
        trainer.setEmail(request.getEmail());
        trainer.setPhone(request.getPhone());
        trainer.setSpecialization(
                request.getSpecialization()
        );
        trainer.setExperience(
                request.getExperience()
        );
        trainer.setJoiningDate(
                request.getJoiningDate()
        );
        trainer.setAddress(
                request.getAddress()
        );
    }
}