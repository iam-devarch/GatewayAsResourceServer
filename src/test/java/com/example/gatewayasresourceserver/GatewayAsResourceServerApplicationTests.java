package com.example.gatewayasresourceserver;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles(value = "test")
class GatewayAsResourceServerApplicationTests {

	@LocalServerPort
	String port;

	private static WireMockServer wireMockServer;
	@Autowired
	private WebTestClient client;

	@DynamicPropertySource
	static void dynamicProperties(DynamicPropertyRegistry registry) {
		registry.add("backend.services.url.conditional", () ->  String.format("http://localhost:%s", wireMockServer.port() ));
		registry.add("backend.services.url.global", () ->  String.format("http://localhost:%s", wireMockServer.port() ));
		registry.add("spring.cloud.gateway.routes[0].id", () ->  "resource-server" );
		registry.add("spring.cloud.gateway.routes[0].predicates[0]", () ->  "Path=/hello/**" );
		registry.add("spring.cloud.gateway.routes[0].uri", () ->  String.format("http://localhost:%s", wireMockServer.port() ));
	}

	@BeforeAll
	static void init() {
		wireMockServer = new WireMockServer(
				new WireMockConfiguration().port(11122)
		);
		wireMockServer.start();
		WireMock.configureFor("localhost", 11122);
	}


	@BeforeEach
	public void clearLogList() {
		client = WebTestClient.bindToServer()
				.baseUrl("http://localhost:" + port)
				.build();
	}

	@Test
	public void whenCallServiceWithoutLogin_thenThrowsUnauthorized() {
		WebTestClient.ResponseSpec response = client.get()
				.uri("/global/get/resource")
				.exchange();

		response.expectStatus()
				.isUnauthorized();
	}

	@Test
	@WithMockUser
	public void whenCallHelloServiceWithLogin_thenSuccess() {

		stubFor(WireMock.get(urlMatching("/hello/get/resource"))
				.willReturn(aResponse()
						.withStatus(OK.value())
						.withBody("Test Body")));

		WebTestClient.ResponseSpec response = client.get()
				.uri("/hello/get/resource")
				.exchange();

		response.expectStatus()
				.isOk()
				.expectBody(String.class).isEqualTo("Test Body");
	}

	@Test
	@WithMockUser
	public void whenCallServiceThroughGateway_thenGlobalFiltersGetExecuted() {

		stubFor(WireMock.get(urlMatching("/global/get/resource"))
				.willReturn(aResponse()
						.withStatus(OK.value())
						.withBody("Test Body")));

		WebTestClient.ResponseSpec response = client.get()
				.uri("/global/get/resource")
				.exchange();

		response.expectStatus()
				.isOk()
				.expectBody(String.class)
				.isEqualTo("Test Body");


	}

	@Test
	@WithMockUser
	public void givenRequestWithConditionalPath_whenCallServiceThroughGateway_thenAllConfiguredFiltersGetExecuted() {
		stubFor(WireMock.get(urlMatching("/conditional/get/resource"))
				.willReturn(aResponse()
						.withStatus(OK.value())
						.withBody("Test Body")));

		WebTestClient.ResponseSpec response = client.get()
				.uri("/conditional/get/resource")
				.exchange();

		response.expectStatus()
				.isOk()
				.expectBody(String.class)
				.isEqualTo("Test Body");


	}

}
