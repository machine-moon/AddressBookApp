package org.example.addressbook;

import java.util.Map;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
class RestApiIntegrationTest {

	@Autowired
	private RestTestClient client;

	@Test
	void createBuddyThenListBuddies() {
		createBuddy("Alice", "613-555-0101");
		createBuddy("Bob", "613-555-0102");

		client.get().uri("/buddies")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.length()").isEqualTo(2)
				.jsonPath("$[0].name").isEqualTo("Alice")
				.jsonPath("$[0].phoneNumber").isEqualTo("613-555-0101")
				.jsonPath("$[1].name").isEqualTo("Bob");
	}

	@Test
	void buildAddressBookAndRenderIt() {
		long alice = createBuddy("Alice", "613-555-0101");
		long bob = createBuddy("Bob", "613-555-0102");
		long charlie = createBuddy("Charlie", "613-555-0103");
		long addressBook = createAddressBook();

		addBuddy(addressBook, alice);
		addBuddy(addressBook, bob);
		addBuddy(addressBook, charlie);

		client.get().uri("/addressBooks/{id}/buddies", addressBook)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.length()").isEqualTo(3)
				.jsonPath("$[0].name").isEqualTo("Alice")
				.jsonPath("$[1].name").isEqualTo("Bob")
				.jsonPath("$[2].name").isEqualTo("Charlie");

		client.get().uri("/addressbooks/{id}", addressBook)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML)
				.expectBody(String.class)
				.value(html -> assertThat(html).contains("Alice", "Bob", "Charlie"));
	}

	@Test
	void unknownAddressBookReturnsServerError() {
		client.get().uri("/addressBooks/{id}/buddies", 999999)
				.exchange()
				.expectStatus().is5xxServerError();
	}

	private long createBuddy(String name, String phoneNumber) {
		Map<?, ?> body = client.post().uri("/buddies")
				.contentType(MediaType.APPLICATION_JSON)
				.body(new BuddyInfo(name, phoneNumber))
				.exchange()
				.expectStatus().isOk()
				.expectBody(Map.class)
				.returnResult()
				.getResponseBody();
		assertThat(body).isNotNull();
		return ((Number) body.get("id")).longValue();
	}

	private long createAddressBook() {
		Map<?, ?> body = client.post().uri("/addressBooks")
				.exchange()
				.expectStatus().isOk()
				.expectBody(Map.class)
				.returnResult()
				.getResponseBody();
		assertThat(body).isNotNull();
		return ((Number) body.get("id")).longValue();
	}

	private void addBuddy(long addressBookId, long buddyId) {
		client.put().uri("/addressBooks/{id}/buddies/{buddyId}", addressBookId, buddyId)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.id").exists()
				.jsonPath("$.buddies").isArray();
	}

}
