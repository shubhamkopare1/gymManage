package gym_management_backend.gymmanagement.dto.response;


import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainerResponseDto {

    private UUID id;

    private String name;

    private String email;

    private String phone;

    private String specialization;

    private String experience;

    private LocalDate joiningDate;

    private String address;

    private boolean active;

    private UUID gymId;

    private String gymName;
}
