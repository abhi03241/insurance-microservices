package com.java.insurance.app;

import com.java.insurance.app.mail.NotificationClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.TaskScheduler;


@SpringBootTest
class InsuranceAppApplicationTests {

	@Autowired
	private TaskScheduler taskScheduler;

	@Autowired
	private NotificationClient notificationClient;

	@Test
	void contextLoads() {
	}

}
