package gym_management_backend.gymmanagement.dto.request;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RazorpayWebhookRequestDto {

    private String event;

    private String razorpayPaymentId;

    private String razorpayOrderId;

    private String signature;
}