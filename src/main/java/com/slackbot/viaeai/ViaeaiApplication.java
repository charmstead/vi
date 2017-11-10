package com.slackbot.viaeai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = {"me.ramswaroop.jbot","com.slackbot.viaeai"})
public class ViaeaiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ViaeaiApplication.class, args);
	}
}
