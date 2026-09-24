package gym_management_backend.gymmanagement.mapper;


import gym_management_backend.gymmanagement.dto.response.PaymentResponseDto;
import gym_management_backend.gymmanagement.entity.Payment;

public class PaymentMapper {

    private PaymentMapper() {
    }

    public static PaymentResponseDto toResponse(
            Payment payment
    ) {

        return PaymentResponseDto.builder()
                .id(payment.getId())

                .memberId(
                        payment.getMember().getId()
                )

                .memberName(
                        payment.getMember().getName()
                )

                .subscriptionId(
                        payment.getSubscription() != null
                                ? payment.getSubscription().getId()
                                : null
                )

                .amount(payment.getAmount())

                .paymentMethod(
                        payment.getPaymentMethod()
                )

                .status(
                        payment.getStatus()
                )

                .transactionId(
                        payment.getTransactionId()
                )
                .gatewayOrderId(
                        payment.getGatewayOrderId()
                )

                .gatewayPaymentId(
                        payment.getGatewayPaymentId()
                )
                .notes(
                        payment.getNotes()
                )

                .paymentDate(
                        payment.getPaymentDate()
                )

                .gymId(
                        payment.getGym().getId()
                )

                .gymName(
                        payment.getGym().getName()
                )

                .build();
    }
}
