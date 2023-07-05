package com.techpractice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

//@SpringBootApplication
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@ComponentScan("com.techpractice")
public class ReviewServiceApplication {

	private static final Logger LOG = LoggerFactory.getLogger(ReviewServiceApplication.class);
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(ReviewServiceApplication.class, args);
		String mysqlUri = context.getEnvironment().getProperty("spring.datasource.url");
		LOG.info("Connected to MySQL: " + mysqlUri);
	}

}
