package gym_management_backend.gymmanagement.dto.response;


import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponseDto {

    private int status;

    private String message;

    private LocalDateTime timestamp;

    private String path;

    private Map<String, String> errors;
}