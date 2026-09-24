package gym_management_backend.gymmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class   GymmanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(GymmanagementApplication.class, args);
	}

}
