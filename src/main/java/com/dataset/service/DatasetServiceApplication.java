package com.dataset.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"com.dataset.service.entity"})
public class DatasetServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DatasetServiceApplication.class, args);
	}

}
