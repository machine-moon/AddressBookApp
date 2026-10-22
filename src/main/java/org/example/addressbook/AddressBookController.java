package org.example.addressbook;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AddressBookController {

    private final AddressBookRepository addressBooks;
    private final BuddyInfoRepository buddies;

    public AddressBookController(AddressBookRepository addressBooks, BuddyInfoRepository buddies) {
        this.addressBooks = addressBooks;
        this.buddies = buddies;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("addressBooks", addressBooks.findAll());
        return "index";
    }

    @PostMapping("/addressbooks")
    public String createAddressBook() {
        addressBooks.save(new AddressBook());
        return "redirect:/";
    }

    @GetMapping("/addressbooks/{id}")
    public String addressBook(@PathVariable Long id, Model model) {
        model.addAttribute("addressBook", addressBooks.findById(id).orElseThrow());
        return "addressbook";
    }

    @PostMapping("/addressbooks/{id}/buddies")
    public String addBuddy(@PathVariable Long id, @ModelAttribute BuddyInfo buddy) {
        AddressBook addressBook = addressBooks.findById(id).orElseThrow();
        addressBook.addBuddy(buddies.save(buddy));
        addressBooks.save(addressBook);
        return "redirect:/addressbooks/" + id;
    }
}
