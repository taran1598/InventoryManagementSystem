package com.example.inventorymanagementsystem.item;

import com.example.inventorymanagementsystem.exceptions.ItemException;
import com.example.inventorymanagementsystem.exceptions.ReduceItemException;
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
    public Item createOrUpdateItem(Item item, int oldTotalQuantity) throws ReduceItemException {
        int newQuantityNotInWarehouse = this.updateQuantityNotInWarehouse(item.getTotalQuantity(), item.getQuantityNotInWarehouse(), oldTotalQuantity);
        if (newQuantityNotInWarehouse < 0) {
            throw new ReduceItemException("Cannot reduce quantity not in warehouse: " + newQuantityNotInWarehouse + "below zero");
        }
        item.setQuantityNotInWarehouse(newQuantityNotInWarehouse);
        return this.itemRepository.save(item);
    }

    /**
     * Get a list of all the items saved to databse
     * @return : List of all items saved in database
     */
    public List<Item> getItems() {
        return this.itemRepository.findAll();
    }

    public Item getItem(long id) throws ItemException {
        return this.itemRepository.findById(id)
                .orElseThrow(() -> new ItemException("No Item found with id: " + id));
    }

    public List<Item> getItems(List<Long> itemId) {
        return this.itemRepository.findAllById(itemId);
    }


    public void deleteItems() {
        this.itemRepository.deleteAll();
    }

    public void deleteItem(long id) {
        this.itemRepository.deleteById(id);
    }


    // returns value of quantityNotInWarehouse to update to.
    public int updateQuantityNotInWarehouse(int newTotalQuantity, int quantityNotInWarehouse, int oldTotalQuantity) {
        int valueOfQuantityChanged = newTotalQuantity - oldTotalQuantity;
        return quantityNotInWarehouse + valueOfQuantityChanged;
    }


    public boolean canReduceQuantity(int newTotalQuantity, int quantityNotInWarehouse, int oldTotalQuantity) {
        int valueOfQuantityReduced = newTotalQuantity - oldTotalQuantity;
        return (valueOfQuantityReduced + quantityNotInWarehouse) >= 0;
    }






}
