package com.example.apigatewayoauthclientredissession.controller;

import static com.example.apigatewayoauthclientredissession.security.SecurityConfig.AUTH_ENTRYPOINT_HEADER_NAME;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.example.apigatewayoauthclientredissession.security.EmptyAuthentication;
import com.example.apigatewayoauthclientredissession.security.LoginLinksProvider;

import reactor.core.publisher.Mono;

/**
 * How can SPA applications redirect users to login page: Need to perform R&D to
 * check what happens when a session has expired and ajax call is made - what
 * response gateway will return and how to redirect the user to the login page.
 * 
 * For UI SPA applications to check whether a user is already authenticated or
 * not, we can expose an API like /whoami: We can expose a '/whoami' endpoint
 * that a client app can call to figure out whether the user is authenticated or
 * not. If the user is authenticated, the endpoint returns status 200 with the
 * user details payload. Otherwise, 401 status is returned with the login page
 * URL in a header. If 401 is returned then the UI can redirect or set the
 * location in the browser to the auth entry point page. This location is
 * returned using a custom header X-auth-entrypoint. Redirecting to the IDP
 * login page after the user reaches '/oauth2/authorization/' where - in our
 * case - the registration name is 'mywebclient'.
 * 
 * UI code, before the application starts, we call '/whoami' api and store the
 * result for later usage. Once the user reaches the protected section of the
 * app, the protected UI component will consume the whoami call result and it
 * will either let the user in or redirect the user to the login page.
 * 
 * To improve the user experience, we store the original page URL requested by
 * the user when the whoami call returns 401. Then, after the user comes back
 * authenticated, we can redirect the user back to the stored URL.
 * 
 * Let’s see what an example : user scenario would look like. We assume that the
 * user hasn't been authenticated yet. The user wants to see the `/customers`
 * page. If we have looked at the portal behaviour for `/customers` we would
 * also notice that for an authenticated user, it will try to fetch data from
 * the `/api/customers` endpoint.
 *
 * 
 */
@RestController
@RequestMapping("/whoami")
public class WhoAmIController {

	@Autowired
	private LoginLinksProvider loginLinksProvider;

	@GetMapping()
	private Mono<ResponseEntity<Map<String, Object>>> whoami(ServerWebExchange exchange) {

		return exchange.getPrincipal().cast(Authentication.class).defaultIfEmpty(new EmptyAuthentication()).map(p -> {
			if (p.isAuthenticated()) {
				return new ResponseEntity<>(((OAuth2AuthenticationToken) p).getPrincipal().getAttributes(),
						HttpStatus.OK);
			} else {
				exchange.getResponse().getHeaders().add(AUTH_ENTRYPOINT_HEADER_NAME, loginLinksProvider.provide());
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}
		});
	}

}
