package gym_management_backend.gymmanagement.controller;

import gym_management_backend.gymmanagement.dto.request.TrainerRequestDto;
import gym_management_backend.gymmanagement.dto.response.MemberResponseDto;
import gym_management_backend.gymmanagement.dto.response.TrainerResponseDto;
import gym_management_backend.gymmanagement.service.TrainerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/gym/trainers")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerService trainerService;

    @PostMapping
    public ResponseEntity<TrainerResponseDto> addTrainer(
            @Valid @RequestBody TrainerRequestDto request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        trainerService.addTrainer(request)
                );
    }

    @GetMapping
    public ResponseEntity<List<TrainerResponseDto>>
    getAllTrainers() {

        return ResponseEntity.ok(
                trainerService.getAllTrainers()
        );
    }

    @GetMapping("/{trainerId}")
    public ResponseEntity<TrainerResponseDto>
    getTrainerById(
            @PathVariable UUID trainerId
    ) {

        return ResponseEntity.ok(
                trainerService.getTrainerById(trainerId)
        );
    }

    @PutMapping("/{trainerId}")
    public ResponseEntity<TrainerResponseDto>
    updateTrainer(
            @PathVariable UUID trainerId,
            @Valid @RequestBody TrainerRequestDto request
    ) {

        return ResponseEntity.ok(
                trainerService.updateTrainer(
                        trainerId,
                        request
                )
        );
    }

    @DeleteMapping("/{trainerId}")
    public ResponseEntity<Void> deleteTrainer(
            @PathVariable UUID trainerId
    ) {

        trainerService.deleteTrainer(trainerId);

        return ResponseEntity.noContent().build();
    }
    @GetMapping("/members")
    public ResponseEntity<List<MemberResponseDto>> getMyMembers() {

        return ResponseEntity.ok(
                trainerService.getMyMembers()
        );
    }
}