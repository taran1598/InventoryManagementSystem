package com.example.inventorymanagementsystem.item;

import com.example.inventorymanagementsystem.exceptions.ItemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemDataService {

    private final ItemRepository itemRepository;

    public ItemDataService(@Autowired ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    /**
     * Saves item to database
     * @param item: Item to save to database
     * @return returns the saved item
     */
    public Item createOrUpdateItem(Item item) {
        return this.itemRepository.save(item);
    }

    public List<Item> createOrUpdateItems(List<Item> item) {
        return this.itemRepository.saveAll(item);
    }

    /**
     * Get a list of all the items saved to databse
     * @return : List of all items saved in database
     */
    public List<Item> getItems() {
        return this.itemRepository.findAll();
    }

    public Item getItem(long id) throws Exception {
        return this.itemRepository.findById(id)
                .orElseThrow(() -> new ItemException("No Item found with id: " + id));
    }

    public List<Item> getItems(List<Long> itemId) {
        return this.itemRepository.findAllById(itemId);
    }

    public void deleteItem(Item item) {
        this.itemRepository.delete(item);
    }

    public void deleteItems() {
        this.itemRepository.deleteAll();
    }

    public void deleteItem(long id) {
        this.itemRepository.deleteById(id);
    }




}
