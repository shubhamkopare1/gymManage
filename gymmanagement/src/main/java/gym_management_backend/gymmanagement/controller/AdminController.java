package gym_management_backend.gymmanagement.controller;

import gym_management_backend.gymmanagement.dto.response.AdminDashboardResponseDto;
import gym_management_backend.gymmanagement.dto.response.AdminGymResponseDto;
import gym_management_backend.gymmanagement.dto.response.AdminUserResponseDto;
import gym_management_backend.gymmanagement.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardResponseDto>
    getDashboard() {

        return ResponseEntity.ok(
                adminService.getDashboard()
        );
    }

    @GetMapping("/gyms")
    public ResponseEntity<List<AdminGymResponseDto>>
    getAllGyms() {

        return ResponseEntity.ok(
                adminService.getAllGyms()
        );
    }

    @GetMapping("/gyms/{gymId}")
    public ResponseEntity<AdminGymResponseDto>
    getGymById(
            @PathVariable UUID gymId
    ) {

        return ResponseEntity.ok(
                adminService.getGymById(gymId)
        );
    }

    @PutMapping("/gyms/{gymId}/activate")
    public ResponseEntity<AdminGymResponseDto>
    activateGym(
            @PathVariable UUID gymId
    ) {

        return ResponseEntity.ok(
                adminService.activateGym(gymId)
        );
    }

    @PutMapping("/gyms/{gymId}/deactivate")
    public ResponseEntity<AdminGymResponseDto>
    deactivateGym(
            @PathVariable UUID gymId
    ) {

        return ResponseEntity.ok(
                adminService.deactivateGym(gymId)
        );
    }

    @GetMapping("/users")
    public ResponseEntity<List<AdminUserResponseDto>>
    getAllUsers() {

        return ResponseEntity.ok(
                adminService.getAllUsers()
        );
    }

    @GetMapping("/gyms/{gymId}/users")
    public ResponseEntity<List<AdminUserResponseDto>>
    getUsersByGym(
            @PathVariable UUID gymId
    ) {

        return ResponseEntity.ok(
                adminService.getUsersByGym(gymId)
        );
    }
}