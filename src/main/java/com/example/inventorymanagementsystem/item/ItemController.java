package com.example.inventorymanagementsystem.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@Controller
public class ItemController {

    private final ItemDataService itemService;

    public ItemController(@Autowired ItemDataService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/items")
    public String getItems(Model model) {
        model.addAttribute("items", this.itemService.getItems());
        return "item.html";
    }

    @GetMapping("/items/{id}")
    public Item getItem(@PathVariable long id) throws Exception {
        return this.itemService.getItem(id);
    }

    @PostMapping("/item")
    public String saveItem(Item item) {
        this.itemService.createOrUpdateItem(item);

        return "redirect:/items";
    }

    @PostMapping("/items")
    public List<Item> saveAllItems(@RequestBody List<Item> items) {
        return this.itemService.createOrUpdateItems(items);
    }

    @DeleteMapping("/items")
    public void deleteAllItems() {
        this.itemService.deleteItems();
    }

    @DeleteMapping("/items/{id}")
    public void deleteItem(@PathVariable long id) {
        this.itemService.deleteItem(id);
    }

    @GetMapping("/addItemsPage")
    public String goToItemsPage(Model model) {
        Item newItem = new Item();
        model.addAttribute("item", newItem);
        return "addItemForm";
    }

}
