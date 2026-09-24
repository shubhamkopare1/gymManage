package gym_management_backend.gymmanagement.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceptionistRequestDto {

    @NotBlank(message = "Receptionist name is required")
    private String name;

    @Email(message = "Invalid email")
    @NotBlank(message = "Receptionist email is required")
    private String email;

    private String phone;

    private String address;

    private LocalDate joiningDate;
    private String loginEmail;

    private String loginPassword;

}