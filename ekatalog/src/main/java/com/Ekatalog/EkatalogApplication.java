package com.Ekatalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class EkatalogApplication {

	public static void main(String[] args) {
		SpringApplication.run(EkatalogApplication.class, args);
		System.out.println("E-Katalog Berhasil");
	}

}
