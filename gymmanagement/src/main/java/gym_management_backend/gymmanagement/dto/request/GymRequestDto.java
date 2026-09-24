package gym_management_backend.gymmanagement.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GymRequestDto {

    @NotBlank(message = "Gym name is required")
    private String name;

    @Email(message = "Invalid gym email")
    @NotBlank(message = "Gym email is required")
    private String email;

    private String phone;

    private String address;

    private String city;

    private String state;

    private String pincode;

    private String logoUrl;
}
