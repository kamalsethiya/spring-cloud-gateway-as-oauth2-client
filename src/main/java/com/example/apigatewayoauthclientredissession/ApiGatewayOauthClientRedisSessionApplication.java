package com.example.apigatewayoauthclientredissession;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ApiGatewayOauthClientRedisSessionApplication {

		public static void main(String[] args) {
			SpringApplication.run(ApiGatewayOauthClientRedisSessionApplication.class, args);
		}
}
