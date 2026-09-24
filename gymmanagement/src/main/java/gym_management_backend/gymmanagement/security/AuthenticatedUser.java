package gym_management_backend.gymmanagement.security;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class AuthenticatedUser {

    private UUID userId;

    private String email;

    private String role;

    private UUID gymId;
}
