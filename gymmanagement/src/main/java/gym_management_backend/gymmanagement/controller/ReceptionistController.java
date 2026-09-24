package gym_management_backend.gymmanagement.controller;

import gym_management_backend.gymmanagement.dto.request.ReceptionistRequestDto;
import gym_management_backend.gymmanagement.dto.response.ReceptionistResponseDto;
import gym_management_backend.gymmanagement.service.ReceptionistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/gym/receptionists")
@RequiredArgsConstructor
public class ReceptionistController {

    private final ReceptionistService receptionistService;

    @PostMapping
    public ResponseEntity<ReceptionistResponseDto>
    addReceptionist(
            @Valid @RequestBody
            ReceptionistRequestDto request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        receptionistService.addReceptionist(
                                request
                        )
                );
    }

    @GetMapping
    public ResponseEntity<List<ReceptionistResponseDto>>
    getAllReceptionists() {

        return ResponseEntity.ok(
                receptionistService.getAllReceptionists()
        );
    }

    @GetMapping("/{receptionistId}")
    public ResponseEntity<ReceptionistResponseDto>
    getReceptionistById(
            @PathVariable UUID receptionistId
    ) {

        return ResponseEntity.ok(
                receptionistService.getReceptionistById(
                        receptionistId
                )
        );
    }

    @PutMapping("/{receptionistId}")
    public ResponseEntity<ReceptionistResponseDto>
    updateReceptionist(
            @PathVariable UUID receptionistId,
            @Valid @RequestBody
            ReceptionistRequestDto request
    ) {

        return ResponseEntity.ok(
                receptionistService.updateReceptionist(
                        receptionistId,
                        request
                )
        );
    }

    @DeleteMapping("/{receptionistId}")
    public ResponseEntity<Void> deleteReceptionist(
            @PathVariable UUID receptionistId
    ) {

        receptionistService.deleteReceptionist(
                receptionistId
        );

        return ResponseEntity.noContent().build();
    }
}
