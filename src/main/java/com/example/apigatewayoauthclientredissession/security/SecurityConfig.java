package com.example.apigatewayoauthclientredissession.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.server.WebSessionServerOAuth2AuthorizedClientRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	public final static String AUTH_ENTRYPOINT_HEADER_NAME = "X-auth-entrypoint";
	
//	@Autowired
//	private AuthenticationFailureHandler authenticationFailureHandler;
//	
//	@Autowired
//	private AuthenticationSuccessHandler authenticationSuccessHandler;
//


	/**
	 * We need to make spring security work with a distributed session so that user
	 * session will work even we deploy multiple instances of our gateway service
	 * behind a LB. If gateway service will keep session in its memory then only one
	 * gateway service instance will have session info and other gateway service
	 * instance will not be aware of that already existing user session and will try
	 * authenticate user again. Hence, to solve this distributed session management
	 * issue we can save authenticated user session details (access token etc) in
	 * redis or database. ServerOAuth2AuthorizedClientRepository class is
	 * responsible to maintaining the user session and it has multiple
	 * implementations WebSessionServerOAuth2AuthorizedClientRepository and
	 * AuthenticatedPrincipalServerOAuth2AuthorizedClientRepository. As per the
	 * articles in the google, default implementation  is
	 * AuthenticatedPrincipalServerOAuth2AuthorizedClientRepository and this class
	 * stores user session data in the service instance memory. Which means this
	 * implementation will not work in distributed environments when we are running
	 * with multiple instances of gateway service. To solve this issue we will use
	 * WebSessionServerOAuth2AuthorizedClientRepository implementation because this
	 * implementation stores user session in the HttpSession object which can be
	 * persisted to redis using spring session library of redis.
	 * 
	 * During oauth grant_type flow, Spring oAuth client library also uses http
	 * session to store authorization request (which contains state parameter). This
	 * stored informtion is used during token exchange for state parameter
	 * verification and code exchange. Default implementation which Spring uses for
	 * this purpose is HttpSessionOAuth2AuthorizationRequestRepository which
	 * internally uses Http Session to store this information. Which means this
	 * implementation HttpSessionOAuth2AuthorizationRequestRepository will work
	 * smoothly with SPring Session using redis.
	 * 
	 * 
	 * Most of the spring security classes that store data are already implemented
	 * using WebSession. The only one that is not is
	 * {@link ServerOAuth2AuthorizedClientRepository} so we define that bean
	 * ourselves.
	 * 
	 * @return ServerOAuth2AuthorizedClientRepository
	 */
	@Bean
	public ServerOAuth2AuthorizedClientRepository authorizedClientRepository() {
		return new WebSessionServerOAuth2AuthorizedClientRepository();
	}

//	@Bean
//	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
//		return http.formLogin(ServerHttpSecurity.FormLoginSpec::disable)
//				.authorizeExchange(ae -> ae.anyExchange().permitAll())
//				.oauth2Login(l -> l.authorizedClientRepository(authorizedClientRepository())).csrf().disable().build();
//	}

	//working fine below
	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
		// @formatter:off
	     // Below we are allowing /users/status/check/unprotected, health and whoami APIs to bypass authentication and all other requests must be authenticated. 
		http.authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec.pathMatchers("/users/status/check/unprotected","/actuator/health","/whoami")
				.permitAll()
				//.pathMatchers("/actuator/gateway/routes").hasAnyRole("developer","admin")
				.anyExchange().authenticated()).oauth2Login();
		return http.build();
		// @formatter:on
	}
	
	//Implemented for ajax call so that a custom header with login URL can be returned with unauthenticated request and status code 401 
//	 @Bean
//	  public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
//	    return http
//	      .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
//	      .authorizeExchange(ae -> ae
//	        .anyExchange()
//	        .permitAll()
//	      )
//	      .oauth2Login(l -> l
//	        .authorizedClientRepository(authorizedClientRepository())
//	        .authenticationSuccessHandler(authenticationSuccessHandler)
//	        .authenticationFailureHandler(authenticationFailureHandler)
//	      )
////	      .logout(l -> l
////	        .logoutSuccessHandler(logoutSuccessHandler)
////	      )
//	      .csrf()
//	      .disable()
//	      .build();
//	  }
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
//		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:8181"));
//		configuration.setAllowedOrigins(Arrays.asList("http://api.manage.local", "http://ui.manage.local"));
		configuration.setAllowedMethods(Arrays.asList("*"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
//		configuration.setAllowedHeaders(Arrays.asList("Origin", "Authorization", "Content-Type", "Accept", "cookie", "X-Requested-With"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
	
	
//	/**
//	 * Create the configuration class where we define the route configurations in java class. Gateway Handler resolves route configurations by using RouteLocator Bean.
//
//	 * @param builder
//	 * @return
//	 */
//	@Bean
//    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route(r -> r.path("/employee/**")
//                        .uri("http://localhost:8081/")
//                        .id("employeeModule"))
//
//                .route(r -> r.path("/consumer/**")
//                        .uri("http://localhost:8082/")
//                        .id("consumerModule"))
//                .build();
//    }
}