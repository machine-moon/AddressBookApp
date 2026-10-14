package org.example.addressbook;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AddressBookRestController {

    private final AddressBookRepository addressBooks;
    private final BuddyInfoRepository buddies;

    public AddressBookRestController(AddressBookRepository addressBooks, BuddyInfoRepository buddies) {
        this.addressBooks = addressBooks;
        this.buddies = buddies;
    }

    @PostMapping("/addressBooks")
    public AddressBook create() {
        return addressBooks.save(new AddressBook());
    }

    @PutMapping("/addressBooks/{id}/buddies/{buddyId}")
    public AddressBook addBuddy(@PathVariable Long id, @PathVariable Long buddyId) {
        AddressBook addressBook = addressBooks.findById(id).orElseThrow();
        addressBook.addBuddy(buddies.findById(buddyId).orElseThrow());
        return addressBooks.save(addressBook);
    }

    @GetMapping("/addressBooks/{id}/buddies")
    public List<BuddyInfo> buddies(@PathVariable Long id) {
        return addressBooks.findById(id).orElseThrow().getBuddies();
    }
}
