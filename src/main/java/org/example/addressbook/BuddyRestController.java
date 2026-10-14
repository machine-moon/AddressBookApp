package org.example.addressbook;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BuddyRestController {

    private final BuddyInfoRepository buddies;

    public BuddyRestController(BuddyInfoRepository buddies) {
        this.buddies = buddies;
    }

    @GetMapping("/buddies")
    public Iterable<BuddyInfo> all() {
        return buddies.findAll();
    }

    @PostMapping("/buddies")
    public BuddyInfo create(@RequestBody BuddyInfo buddy) {
        return buddies.save(buddy);
    }
}
