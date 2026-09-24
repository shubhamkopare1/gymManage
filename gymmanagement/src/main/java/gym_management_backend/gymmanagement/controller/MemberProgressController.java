package gym_management_backend.gymmanagement.controller;

import gym_management_backend.gymmanagement.dto.request.ProgressRequestDto;
import gym_management_backend.gymmanagement.dto.response.ProgressResponseDto;
import gym_management_backend.gymmanagement.service.MemberProgressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/gym/progress")
@RequiredArgsConstructor
public class MemberProgressController {

    private final MemberProgressService progressService;

    @PostMapping
    public ResponseEntity<ProgressResponseDto> addProgress(
            @Valid @RequestBody ProgressRequestDto request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        progressService.addProgress(request)
                );
    }

    @GetMapping
    public ResponseEntity<List<ProgressResponseDto>>
    getAllProgress() {

        return ResponseEntity.ok(
                progressService.getAllProgress()
        );
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<ProgressResponseDto>>
    getMemberProgress(
            @PathVariable UUID memberId
    ) {

        return ResponseEntity.ok(
                progressService.getMemberProgress(
                        memberId
                )
        );
    }

    @GetMapping("/{progressId}")
    public ResponseEntity<ProgressResponseDto>
    getProgressById(
            @PathVariable UUID progressId
    ) {

        return ResponseEntity.ok(
                progressService.getProgressById(
                        progressId
                )
        );
    }

    @PutMapping("/{progressId}")
    public ResponseEntity<ProgressResponseDto>
    updateProgress(
            @PathVariable UUID progressId,
            @Valid @RequestBody ProgressRequestDto request
    ) {

        return ResponseEntity.ok(
                progressService.updateProgress(
                        progressId,
                        request
                )
        );
    }

    @DeleteMapping("/{progressId}")
    public ResponseEntity<Void> deleteProgress(
            @PathVariable UUID progressId
    ) {

        progressService.deleteProgress(progressId);

        return ResponseEntity.noContent().build();
    }
}
