//package com.example.apigatewayoauthclientredissession.filters;
//
//import static com.example.apigatewayoauthclientredissession.security.SecurityConfig.AUTH_ENTRYPOINT_HEADER_NAME;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//
//import com.example.apigatewayoauthclientredissession.security.LoginLinksProvider;
//
//import reactor.core.publisher.Mono;
//
//@Configuration
//public class Global401Filter {
//
//	@Autowired
//	private LoginLinksProvider loginLinksProvider;
//
//	@Bean
//	public GlobalFilter postGlobalFilter() {
//		return (exchange, chain) -> chain.filter(exchange).then(Mono.fromRunnable(() -> {
//			ServerHttpRequest request = exchange.getRequest();
//			ServerHttpResponse response = exchange.getResponse();
//			if (HttpStatus.UNAUTHORIZED.equals(response.getStatusCode())) {
//				response.getHeaders().add(AUTH_ENTRYPOINT_HEADER_NAME, loginLinksProvider.provide());
//			}
//		}));
//	}
//
//}
