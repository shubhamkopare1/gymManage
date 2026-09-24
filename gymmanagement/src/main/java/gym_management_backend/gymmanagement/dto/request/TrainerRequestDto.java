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
public class TrainerRequestDto {

    @NotBlank(message = "Trainer name is required")
    private String name;

    @Email(message = "Invalid email")
    @NotBlank(message = "Trainer email is required")
    private String email;

    private String phone;

    private String specialization;

    private String experience;

    private LocalDate joiningDate;

    private String address;
    private String loginEmail;

    private String loginPassword;
}
