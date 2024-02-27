package net.l3mon.LogisticsL3mon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
//@EnableJpaRepositories
public class LogisticsL3monApplication {

	public static void main(String[] args) {
		SpringApplication.run(LogisticsL3monApplication.class, args);
	}

}
