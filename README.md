# spring-cloud-gateway-as-ouath2-client
spring-cloud-gateway-as-ouath2-client

Sequence to start services:
---------------------------
1. Start redis on docker
2. Start KeyCloak server
3. Start DiscoveryService
4. Start spring-cloud-gateway-as-ouath2-client
5. Start ResourceServer
6. Start albums
7. Start photos

There are two API gateway implementations :
---------------------------
1. spring-cloud-gateway-as-ouath2-client - Implementation of Spring Cloud API Gateway and its acting as oauth client for authorization_code flow
2. ApiGateway - Implementation of Spring Cloud API Gateway and just doing routing 

Few test URLS:
----------------
http://localhost:8087/albums
http://localhost:8087/users/status/check
http://localhost:8087/users/status/check/unprotected
http://localhost:8087/index.html
http://localhost:8087/index-cookie-domain
http://localhost:8087/index-cookie-localhost
