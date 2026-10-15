package org.example.addressbook;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class BuddyInfo {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String phoneNumber;
    private String address;

    public BuddyInfo() {
    }

    public BuddyInfo(String name, String phoneNumber, String address) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return name + " - " + phoneNumber + " - " + address;
    }
}
