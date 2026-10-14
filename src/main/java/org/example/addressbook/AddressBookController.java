package org.example.addressbook;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class AddressBookController {

    private final AddressBookRepository addressBooks;

    public AddressBookController(AddressBookRepository addressBooks) {
        this.addressBooks = addressBooks;
    }

    @GetMapping("/addressbooks/{id}")
    public String addressBook(@PathVariable Long id, Model model) {
        model.addAttribute("addressBook", addressBooks.findById(id).orElseThrow());
        return "addressbook";
    }
}
