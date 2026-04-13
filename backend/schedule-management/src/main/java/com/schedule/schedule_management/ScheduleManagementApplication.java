package com.schedule.schedule_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ScheduleManagementApplication {
	public static void main(String[] args) {
		SpringApplication.run(ScheduleManagementApplication.class, args);
	}
}