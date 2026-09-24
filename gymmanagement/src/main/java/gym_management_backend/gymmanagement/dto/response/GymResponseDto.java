package gym_management_backend.gymmanagement.dto.response;


import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GymResponseDto {

    private UUID id;

    private String name;

    private String email;

    private String phone;

    private String address;

    private String city;

    private String state;

    private String pincode;

    private String logoUrl;

    private boolean active;
}
