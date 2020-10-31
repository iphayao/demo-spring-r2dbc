package com.iphayao.demo;

import com.iphayao.demo.customer.Customer;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DemoApplicationTests {
	@Autowired
	WebTestClient webClient;

	@Test
	@Order(0)
	void test_get_customers_expect_list_of_customer() {
		webClient.get().uri("/customers")
				.header(HttpHeaders.ACCEPT, "application/json")
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBodyList(Customer.class);

	}

	@Test
	@Order(1)
	void test_post_customers_expect_return_customer_with_id() {
		Customer testBody = new Customer();
		testBody.setFirstName("John");
		testBody.setLastName("Doe");

		webClient.post().uri("/customers")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.body(Mono.just(testBody), Customer.class)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.id").isNotEmpty()
				.jsonPath("$.firstName").isEqualTo(testBody.getFirstName())
				.jsonPath("$.lastName").isEqualTo(testBody.getLastName());
	}

	@Test
	@Order(2)
	void test_get_by_id_expect_return_customer_of_id() {
		Customer expectBody = new Customer();
		expectBody.setId(1L);
		expectBody.setFirstName("John");
		expectBody.setLastName("Doe");

		webClient.get().uri("/customers/{id}", 1)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.id").isNotEmpty()
				.jsonPath("$.firstName").isEqualTo(expectBody.getFirstName())
				.jsonPath("$.lastName").isEqualTo(expectBody.getLastName());
	}

	@Test
	@Order(3)
	void test_get_by_id_expect_return_not_found() {
		webClient.get().uri("/customers/{id}", 100)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isNotFound();
	}


	@Test
	@Order(4)
	void test_put_customers_expect_return_customer_with_data_changed() {
		Customer testBody = new Customer();
		testBody.setFirstName("Jan");
		testBody.setLastName("Doe");

		webClient.put().uri("/customers/{id}", 1)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.body(Mono.just(testBody), Customer.class)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.id").isEqualTo(1)
				.jsonPath("$.firstName").isEqualTo(testBody.getFirstName())
				.jsonPath("$.lastName").isEqualTo(testBody.getLastName());
	}

	@Test
	@Order(5)
	void test_delete_customers_expect_customer_deleted() {
		webClient.delete().uri("/customers/{id}", 1)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk();
	}
}
