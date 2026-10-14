package org.example.addressbook;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
public class AddressBook {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany
    private List<BuddyInfo> buddies = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void addBuddy(BuddyInfo buddy) {
        buddies.add(buddy);
    }

    public List<BuddyInfo> getBuddies() {
        return buddies;
    }
}
