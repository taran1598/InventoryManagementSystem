package com.example.inventorymanagementsystem.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class ItemController {

    private final ItemService itemService;

    public ItemController(@Autowired ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/items")
    public List<Item> getItems() {
        return this.itemService.getItems();
    }

    @GetMapping("/items/{id}")
    public Item getItem(@PathVariable long id) {
        return this.itemService.getItem(id);
    }

    @PostMapping("/item")
    public Item saveItem(@RequestBody Item item) {
        return this.itemService.saveItem(item);
    }

    @PostMapping("/items")
    public List<Item> saveAllItems(@RequestBody List<Item> items) {
        return this.itemService.saveItems(items);
    }

    @DeleteMapping("/items")
    public void deleteAllItems() {
        this.itemService.deleteItems();
    }

}
