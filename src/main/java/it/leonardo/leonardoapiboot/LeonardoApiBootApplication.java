package it.leonardo.leonardoapiboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

@EntityScan("it.leonardo.leonardoapiboot.entity")
@SpringBootApplication
public class LeonardoApiBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeonardoApiBootApplication.class, args);
	}

}
