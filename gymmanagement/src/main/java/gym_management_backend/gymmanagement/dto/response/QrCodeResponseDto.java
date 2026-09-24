package gym_management_backend.gymmanagement.dto.response;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QrCodeResponseDto {

    private String qrCode;
}