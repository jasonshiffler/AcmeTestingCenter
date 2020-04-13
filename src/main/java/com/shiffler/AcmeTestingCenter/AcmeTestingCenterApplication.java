package com.shiffler.AcmeTestingCenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class AcmeTestingCenterApplication {

	public static void main(String[] args) {
		SpringApplication.run(AcmeTestingCenterApplication.class, args);
	}


}
