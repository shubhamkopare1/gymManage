package gym_management_backend.gymmanagement.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {

    @NotBlank(message = "Owner name is required")
    private String name;

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Gym name is required")
    private String gymName;

    @Email(message = "Invalid gym email")
    @NotBlank(message = "Gym email is required")
    private String gymEmail;

    private String phone;

    private String address;

    private String city;

    private String state;

    private String pincode;
}