package treasure_hunt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class that starts the application as a stand-alone application
 */

@SpringBootApplication
// @ComponentScan(basePackageClasses = MetricsController.class)
public class AppStart {
	public static void main(String[] args) {
		SpringApplication.run(AppStart.class);
	}
}
