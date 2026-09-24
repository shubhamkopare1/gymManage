package gym_management_backend.gymmanagement.config;


import gym_management_backend.gymmanagement.entity.User;
import gym_management_backend.gymmanagement.entity.enums.Role;
import gym_management_backend.gymmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (userRepository.findByEmail("superadmin@gmail.com").isEmpty()) {

            User superAdmin = User.builder()
                    .name("Super Admin")
                    .email("superadmin@gmail.com")
                    .password(passwordEncoder.encode("Admin@123"))
                    .role(Role.SUPER_ADMIN)
                    .gym(null)
                    .active(true)
                    .build();

            userRepository.save(superAdmin);

            System.out.println("SUPER ADMIN CREATED");
        }
    }
}