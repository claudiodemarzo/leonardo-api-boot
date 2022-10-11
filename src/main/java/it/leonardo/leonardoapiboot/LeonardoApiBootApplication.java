package it.leonardo.leonardoapiboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan("it.leonardo.leonardoapiboot.entity")
@SpringBootApplication
public class LeonardoApiBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeonardoApiBootApplication.class, args);
	}



}
