package gym_management_backend.gymmanagement.controller;


import gym_management_backend.gymmanagement.dto.request.GymRequestDto;
import gym_management_backend.gymmanagement.dto.response.GymResponseDto;
import gym_management_backend.gymmanagement.service.GymService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gym")
@RequiredArgsConstructor
public class GymController {

    private final GymService gymService;

    @GetMapping("/profile")
    public ResponseEntity<GymResponseDto> getMyGym() {

        return ResponseEntity.ok(
                gymService.getMyGym()
        );
    }

    @PutMapping("/profile")
    public ResponseEntity<GymResponseDto> updateMyGym(
            @Valid @RequestBody GymRequestDto request
    ) {

        return ResponseEntity.ok(
                gymService.updateMyGym(request)
        );
    }
}