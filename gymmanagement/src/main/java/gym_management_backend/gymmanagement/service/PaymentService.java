package gym_management_backend.gymmanagement.service;


import gym_management_backend.gymmanagement.dto.request.PaymentOrderRequestDto;
import gym_management_backend.gymmanagement.dto.request.PaymentRequestDto;
import gym_management_backend.gymmanagement.dto.request.PaymentVerificationRequestDto;
import gym_management_backend.gymmanagement.dto.response.PaymentOrderResponseDto;
import gym_management_backend.gymmanagement.dto.response.PaymentResponseDto;
import gym_management_backend.gymmanagement.entity.enums.PaymentStatus;

import java.util.List;
import java.util.UUID;

public interface PaymentService {

    PaymentResponseDto createPayment(
            PaymentRequestDto request
    );

    List<PaymentResponseDto> getAllPayments();

    List<PaymentResponseDto> getMemberPayments(
            UUID memberId
    );
    PaymentOrderResponseDto createPaymentOrder(
            PaymentOrderRequestDto request
    );
    PaymentResponseDto getPaymentById(
            UUID paymentId
    );
    PaymentResponseDto verifyPayment(
            PaymentVerificationRequestDto request
    );
    PaymentResponseDto updatePaymentStatus(
            UUID paymentId,
            PaymentStatus status
    );
    PaymentResponseDto handleWebhook(
            String payload,
            String signature
    );
}