package com.cmcc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.cmcc.common.utils.JwtTokenUtil;

@SpringBootApplication
@EnableTransactionManagement
@EnableEurekaClient
@EnableFeignClients
public class BootApplication {

private static ApplicationContext applicationContext;
	
	public static void main(String[] args) {
		applicationContext = SpringApplication.run(BootApplication.class, args);
		JwtTokenUtil.setApplicationContext(applicationContext);
	}
}
