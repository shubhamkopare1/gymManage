package gym_management_backend.gymmanagement.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QrAttendanceRequestDto {

    @NotBlank(message = "QR code is required")
    private String qrCode;
}
