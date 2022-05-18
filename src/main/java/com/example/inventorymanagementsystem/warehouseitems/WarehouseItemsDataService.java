package com.example.inventorymanagementsystem.warehouseitems;

import com.example.inventorymanagementsystem.exceptions.AddItemToWarehouseException;
import com.example.inventorymanagementsystem.exceptions.ReduceItemException;
import com.example.inventorymanagementsystem.exceptions.WarehouseItemsException;
import com.example.inventorymanagementsystem.item.Item;
import com.example.inventorymanagementsystem.item.ItemDataService;
import com.example.inventorymanagementsystem.warehouse.Warehouse;
import com.example.inventorymanagementsystem.warehouse.WarehouseDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WarehouseItemsDataService {


    private final ItemDataService itemDataService;
    private final WarehouseItemsRepository warehouseItemsRepository;
    private final WarehouseDataService warehouseDataService;

    public WarehouseItemsDataService(@Autowired WarehouseItemsRepository warehouseItemsRepository,
                                     @Autowired WarehouseDataService warehouseDataService,
                                     @Autowired ItemDataService itemDataService) {
        this.warehouseItemsRepository = warehouseItemsRepository;
        this.warehouseDataService = warehouseDataService;
        this.itemDataService = itemDataService;
    }

    public List<WarehouseItems> getAllWarehouseItems() {
        return this.warehouseItemsRepository.findAll();
    }

    public WarehouseItems getWarehouseItem(WarehouseItemsCompositeKey key) throws Exception {
        return this.warehouseItemsRepository.findById(key)
                .orElseThrow(
                        () -> new WarehouseItemsException(
                                "Cannot find warehouse item with itemId: " + key.getItemId() + "in warehouseId "
                                        + key.getWarehouseId()));
    }

    public void deleteWarehouseItem(WarehouseItemsCompositeKey key) {
        this.warehouseItemsRepository.deleteById(key);
    }

    public List<WarehouseItems> findItemsInWarehouse(long warehouseId) {
        return this.warehouseItemsRepository.findAllByWarehouseId(warehouseId);
    }

    public List<Item> getItemsInWarehouse(List<WarehouseItems> warehouseItemsList) {
        List<Long> itemsInWarehouse = warehouseItemsList.stream()
                .map((WarehouseItems::getItemId)).collect(Collectors.toList());

        return this.itemDataService.getItems(itemsInWarehouse);
    }

    /**
     * Save item to warehouse, if we have enough quantity of the item left (i.e the item is no
     * @param itemId: id of the item
     * @param warehouseId: id of the warehouse
     * @param quantityToSave: amount to save
     */
    @Transactional
    public WarehouseItems saveTimeToWarehouse(long itemId, long warehouseId, int quantityToSave) throws Exception {
        Item itemToSave = validateItem(itemId);
        Warehouse warehouse = validateWarehouse(warehouseId);

        if (canAddItemToWarehouse(warehouse, itemToSave, quantityToSave)) {

            WarehouseItemsCompositeKey warehouseItemsKey = new WarehouseItemsCompositeKey(itemId, warehouseId);
            WarehouseItems warehouseItems = warehouseItemsRepository
                    .findById(warehouseItemsKey)
                    .orElse(new WarehouseItems(itemId, warehouseId, 0));

            // update the quantity of this item we now have in the warehouse
            int newItemQuantity = warehouseItems.getQuantity() + quantityToSave;
            warehouseItems.setQuantity(newItemQuantity);

            itemToSave.setQuantityNotInWarehouse(itemToSave.getQuantityNotInWarehouse() - quantityToSave);

            // update the capacity of the warehouse
            warehouse.setCurrentCapacity(warehouse.getCurrentCapacity() + quantityToSave);


            // add the items to the warehouse and the warehouse to the items
            warehouse.addItem(itemToSave);
            itemToSave.addWarehouse(warehouse);

            // save entities to database
            this.warehouseDataService.createOrUpdateWarehouse(warehouse);
            this.itemDataService.createOrUpdateItem(itemToSave);
            return this.warehouseItemsRepository.save(warehouseItems);


        } else {
            throw new AddItemToWarehouseException("Cannot add item to warehouse due to invalid quantity: " + quantityToSave);
        }
    }

    /**
     * Deletes quantityTODelete items with itemId from the warehouse with warehouseId. itemId and warehouseId will always
     * be unique. If no warehouse/item with warehouseId/itemId is found then nothing will be deleted
     * @param itemId: Id of the item to delete. This is the pk of the item
     * @param warehouseId: Id of the warehouse to delete the item from. This is the pk of the warehouse
     * @param quantityToDelete: Amount to delete from the warehouse. The warehouse must have at least this many items otherwise no deletion will happen
     * @return: WarehouseItems after deletion
     */
    @Transactional
    public void reduceItemFromWarehouse(long itemId, long warehouseId, int quantityToDelete) throws Exception {
        WarehouseItemsCompositeKey warehouseItemsKey = new WarehouseItemsCompositeKey(itemId, warehouseId);
        Item item = validateItem(itemId);
        Warehouse warehouse = validateWarehouse(warehouseId);
        WarehouseItems warehouseItems = this.getWarehouseItem(warehouseItemsKey);

        if (canReduceItemFromWarehouse(warehouse, item, warehouseItems, quantityToDelete)) {

            warehouse.setCurrentCapacity(warehouse.getCurrentCapacity() - quantityToDelete);
            item.setQuantityNotInWarehouse(item.getQuantityNotInWarehouse() + quantityToDelete);

            // update warehouseItems. If we have 0 of itemId in warehouseId then we delete the entry from the warehouse
            warehouseItems.setQuantity(warehouseItems.getQuantity() - quantityToDelete);

            // delete the item if quantity of item in warehouse is 0
            if (warehouseItems.getQuantity() == 0) {
                this.deleteWarehouseItem(warehouseItemsKey);
            } else {
                this.warehouseItemsRepository.save(warehouseItems);
            }

            // save entities to db
            this.itemDataService.createOrUpdateItem(item);
            this.warehouseDataService.createOrUpdateWarehouse(warehouse);
        } else {
            throw new ReduceItemException("Invalid quantity to delete. Trying to delete too much or too little with quantity: " + quantityToDelete);
        }
    }

    private Item validateItem(long itemId) throws Exception {
        return itemDataService.getItem(itemId);
    }

    private Warehouse validateWarehouse(long warehouseId) throws Exception {
        return warehouseDataService.getWarehouse(warehouseId);
    }


    /**
     * Return true if there is enough capacity in the warehouse to store the item and there are enough items remaining not in a warehouse
     */
    private boolean canAddItemToWarehouse(Warehouse warehouse, Item item, int quantityToAdd) {

        int warehouseCapacityAfterAdd = warehouse.getCurrentCapacity() + quantityToAdd;
        int remainingItemQuantityAfterAdd = item.getQuantityNotInWarehouse() - quantityToAdd;

        return warehouseCapacityAfterAdd <= warehouse.getMaxCapacity() &&
                remainingItemQuantityAfterAdd >= 0 &&
                quantityToAdd >= 0;
    }


    private boolean canReduceItemFromWarehouse(Warehouse warehouse, Item item, WarehouseItems warehouseItems, int quantityToDelete) {
        int warehouseCapacityAfterDelete = warehouse.getCurrentCapacity() - quantityToDelete;
        int itemNewQuantity = item.getQuantityNotInWarehouse() + quantityToDelete;
        int itemsInWarehouseAfterDelete = warehouseItems.getQuantity() - quantityToDelete;

        return warehouseCapacityAfterDelete >= 0 && itemNewQuantity <= item.getTotalQuantity() && itemsInWarehouseAfterDelete >= 0;
    }


}
