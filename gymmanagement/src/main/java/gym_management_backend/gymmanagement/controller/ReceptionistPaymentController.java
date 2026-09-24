package gym_management_backend.gymmanagement.controller;

import gym_management_backend.gymmanagement.entity.Payment;
import gym_management_backend.gymmanagement.repository.PaymentRepository;
import gym_management_backend.gymmanagement.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reception/payments")
@RequiredArgsConstructor
public class ReceptionistPaymentController {

    private final PaymentRepository paymentRepository;

    @GetMapping
    public List<Payment> getPayments() {

        AuthenticatedUser user = getUser();

        return paymentRepository
                .findAllByGymId(user.getGymId());
    }

    private AuthenticatedUser getUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        return (AuthenticatedUser)
                authentication.getPrincipal();
    }
}
