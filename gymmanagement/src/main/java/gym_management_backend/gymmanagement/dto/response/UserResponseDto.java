package gym_management_backend.gymmanagement.dto.response;



import gym_management_backend.gymmanagement.entity.enums.Role;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {

    private UUID id;

    private String name;

    private String email;

    private Role role;

    private boolean active;

    private UUID gymId;

    private String gymName;
}
