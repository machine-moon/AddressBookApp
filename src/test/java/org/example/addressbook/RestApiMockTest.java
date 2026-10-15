package org.example.addressbook;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest({ BuddyRestController.class, AddressBookRestController.class })
@AutoConfigureRestTestClient
class RestApiMockTest {

	@Autowired
	private RestTestClient client;

	@MockitoBean
	private BuddyInfoRepository buddies;

	@MockitoBean
	private AddressBookRepository addressBooks;

	@Test
	void postBuddyReturnsTheSavedBuddy() {
		when(buddies.save(any(BuddyInfo.class))).thenAnswer((invocation) -> invocation.getArgument(0));

		client.post().uri("/buddies")
				.contentType(MediaType.APPLICATION_JSON)
				.body("{\"name\":\"Alice\",\"phoneNumber\":\"613-555-0101\",\"address\":\"1 First St\"}")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.name").isEqualTo("Alice")
				.jsonPath("$.phoneNumber").isEqualTo("613-555-0101")
				.jsonPath("$.address").isEqualTo("1 First St");

		verify(buddies).save(any(BuddyInfo.class));
	}

	@Test
	void getBuddiesReturnsAllBuddies() {
		when(buddies.findAll()).thenReturn(List.of(
				new BuddyInfo("Alice", "613-555-0101", "1 First St"),
				new BuddyInfo("Bob", "613-555-0102", "2 Second St")));

		client.get().uri("/buddies")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.length()").isEqualTo(2)
				.jsonPath("$[0].name").isEqualTo("Alice")
				.jsonPath("$[0].phoneNumber").isEqualTo("613-555-0101")
				.jsonPath("$[0].address").isEqualTo("1 First St")
				.jsonPath("$[1].name").isEqualTo("Bob");
	}

	@Test
	void postAddressBookReturnsAnEmptyAddressBook() {
		when(addressBooks.save(any(AddressBook.class))).thenAnswer((invocation) -> invocation.getArgument(0));

		client.post().uri("/addressBooks")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.buddies.length()").isEqualTo(0);

		verify(addressBooks).save(any(AddressBook.class));
	}

	@Test
	void addBuddyToAddressBook() {
		AddressBook addressBook = new AddressBook();
		BuddyInfo alice = new BuddyInfo("Alice", "613-555-0101", "1 First St");
		when(addressBooks.findById(1L)).thenReturn(Optional.of(addressBook));
		when(buddies.findById(1L)).thenReturn(Optional.of(alice));
		when(addressBooks.save(any(AddressBook.class))).thenAnswer((invocation) -> invocation.getArgument(0));

		client.put().uri("/addressBooks/{id}/buddies/{buddyId}", 1, 1)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.buddies.length()").isEqualTo(1)
				.jsonPath("$.buddies[0].name").isEqualTo("Alice")
				.jsonPath("$.buddies[0].address").isEqualTo("1 First St");
	}

	@Test
	void getAddressBookBuddiesReturnsTheBuddies() {
		AddressBook addressBook = new AddressBook();
		addressBook.addBuddy(new BuddyInfo("Alice", "613-555-0101", "1 First St"));
		addressBook.addBuddy(new BuddyInfo("Bob", "613-555-0102", "2 Second St"));
		when(addressBooks.findById(1L)).thenReturn(Optional.of(addressBook));

		client.get().uri("/addressBooks/{id}/buddies", 1)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.length()").isEqualTo(2)
				.jsonPath("$[0].name").isEqualTo("Alice")
				.jsonPath("$[1].name").isEqualTo("Bob");
	}

}
