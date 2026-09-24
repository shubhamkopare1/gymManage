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
public class MemberRequestDto {

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email")
    private String email;

    @NotBlank(message = "Phone is required")
    private String phone;

    private String gender;

    private LocalDate dateOfBirth;

    private String address;

    private String emergencyContact;

    private LocalDate joiningDate;

    @Email(message = "Invalid login email")
    private String loginEmail;

    private String loginPassword;
}