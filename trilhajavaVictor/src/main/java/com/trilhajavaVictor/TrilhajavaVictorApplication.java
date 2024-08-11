package com.trilhajavaVictor;

import com.trilhajavaVictor.principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TrilhajavaVictorApplication implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(TrilhajavaVictorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal();
		principal.exibeMenu();



	}
}
