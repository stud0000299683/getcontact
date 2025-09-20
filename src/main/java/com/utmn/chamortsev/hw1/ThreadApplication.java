package com.utmn.chamortsev.hw1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ThreadApplication {

	public static void main(String[] args) {
		SpringApplication.run(ThreadApplication.class, args);
		System.out.println("Its working!");
	}

}
