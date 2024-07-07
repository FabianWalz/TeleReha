package ehealth.telereha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = "ehealth.telereha")
public class TelerehaApplication {

	public static void main(String[] args) {
		SpringApplication.run(TelerehaApplication.class, args);
	}
}
