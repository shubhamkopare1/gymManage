package gym_management_backend.gymmanagement.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import gym_management_backend.gymmanagement.entity.enums.Role;
import gym_management_backend.gymmanagement.service.SaasAccessService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class GymSubscriptionAccessFilter extends OncePerRequestFilter {

    private final SaasAccessService saasAccessService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String uri = request.getRequestURI();

        if (isBypassPath(uri)) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            filterChain.doFilter(request, response);
            return;
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof AuthenticatedUser authenticatedUser)) {

            filterChain.doFilter(request, response);
            return;
        }

        if (Role.SUPER_ADMIN.name()
                .equals(authenticatedUser.getRole())) {

            filterChain.doFilter(request, response);
            return;
        }

        if (authenticatedUser.getGymId() == null) {

            sendForbiddenResponse(
                    response,
                    "Gym is not associated with this account."
            );

            return;
        }

        boolean active =
                saasAccessService.hasActiveSubscription(
                        authenticatedUser.getGymId()
                );

        if (!active) {

            sendForbiddenResponse(
                    response,
                    "Gym SaaS subscription is inactive or expired. "
                            + "Please renew your subscription."
            );

            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isBypassPath(String uri) {

        return uri.startsWith("/api/auth/")
                || uri.startsWith("/api/gym/saas-subscriptions/")
                || uri.equals("/api/gym/saas-subscriptions")
                || uri.startsWith("/api/gym/saas-plans/")
                || uri.equals("/api/gym/saas-plans")
                || uri.startsWith("/api/admin/")
                || uri.equals("/api/gym/payments/webhook");
    }

    private void sendForbiddenResponse(
            HttpServletResponse response,
            String message
    ) throws IOException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");

        var body = java.util.Map.of(
                "status", 403,
                "message", message
        );

        response.getWriter()
                .write(objectMapper.writeValueAsString(body));
    }
}