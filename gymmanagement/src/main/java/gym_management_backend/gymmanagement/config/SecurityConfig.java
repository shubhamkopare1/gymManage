package gym_management_backend.gymmanagement.config;

import gym_management_backend.gymmanagement.security.GymSubscriptionAccessFilter;
import gym_management_backend.gymmanagement.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final GymSubscriptionAccessFilter gymSubscriptionAccessFilter;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .cors(cors -> {})

                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/api/auth/**"
                        ).permitAll()
                        .requestMatchers(
                                "/api/gym/payments/webhook"
                        ).permitAll()
                        .requestMatchers("/api/webhooks/razorpay").permitAll()
                        .requestMatchers(
                                "/api/admin/**"
                        ).hasRole("SUPER_ADMIN")

                        .requestMatchers(
                                "/api/gym/attendance/**"
                        ).hasAnyRole(
                                "SUPER_ADMIN",
                                "GYM_OWNER",
                                "RECEPTIONIST"
                        )

                        .requestMatchers(
                                "/api/gym/accounts/**"
                        ).hasAnyRole(
                                "SUPER_ADMIN",
                                "GYM_OWNER"
                        )

                        .requestMatchers(
                                "/api/gym/**"
                        ).hasAnyRole(
                                "SUPER_ADMIN",
                                "GYM_OWNER"
                        )

                        .requestMatchers(
                                "/api/trainer/**"
                        ).hasAnyRole(
                                "SUPER_ADMIN",
                                "GYM_OWNER",
                                "TRAINER"
                        )

                        .requestMatchers(
                                "/api/reception/**"
                        ).hasAnyRole(
                                "SUPER_ADMIN",
                                "GYM_OWNER",
                                "RECEPTIONIST"
                        )

                        .requestMatchers(
                                "/api/member/**"
                        ).hasRole("MEMBER")

                        .requestMatchers(
                                "/api/gym/audit-logs/**"
                        ).hasAnyRole(
                                "SUPER_ADMIN",
                                "GYM_OWNER"
                        )
                        .requestMatchers("/api/webhooks/razorpay")
                        .permitAll()

                        .anyRequest()
                        .authenticated()
                );

        http.addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
        );

        http.addFilterAfter(
                gymSubscriptionAccessFilter,
                JwtAuthenticationFilter.class
        );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}