package com.example.inventorymanagementsystem.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class ItemController {

    private final ItemDataService itemService;

    public ItemController(@Autowired ItemDataService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/items")
    public List<Item> getItems() {
        return this.itemService.getItems();
    }

    @GetMapping("/items/{id}")
    public Item getItem(@PathVariable long id) throws Exception {
        return this.itemService.getItem(id);
    }

    @PostMapping("/item")
    public Item saveItem(@RequestBody Item item) {
        return this.itemService.createOrUpdateItem(item);
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

}
