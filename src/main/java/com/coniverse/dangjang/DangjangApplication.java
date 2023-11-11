package com.coniverse.dangjang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import given.apiversion.autoconfigure.EnableApiVersion;

@EnableApiVersion
@EnableScheduling
@EnableJpaAuditing
@EnableAsync
@SpringBootApplication
public class DangjangApplication {
	public static void main(String[] args) {
		SpringApplication.run(DangjangApplication.class, args);
	}
}
