package gym_management_backend.gymmanagement.dto.response;


import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberResponseDto {

    private UUID id;

    private String name;

    private String email;

    private String phone;

    private String gender;

    private LocalDate dateOfBirth;

    private String address;

    private String emergencyContact;

    private LocalDate joiningDate;

    private boolean active;

    private UUID gymId;

    private String gymName;
}
