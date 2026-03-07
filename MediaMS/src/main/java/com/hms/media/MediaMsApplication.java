package com.hms.media;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MediaMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MediaMsApplication.class, args);
	}

}
