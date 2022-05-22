package com.example.inventorymanagementsystem.warehouseitems;

import com.example.inventorymanagementsystem.exceptions.*;
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

    /**
     * Deleting an item should delete the item from the database, as well from the warehouse
     * @param itemId: id of the item to delete
     */
    public void deleteItem(long itemId) {
        List<WarehouseItems> warehouseItemsList = this.findAllItems(itemId);

        warehouseItemsList.forEach(warehouseItems -> {
            // want to update warehouse. Want to also catch exception because we want to move onto next item even if the warehouse does not exist
            try {
                Warehouse warehouse = this.warehouseDataService.getWarehouse(warehouseItems.getWarehouseId());
                warehouse.setCurrentCapacity(warehouse.getCurrentCapacity() - warehouseItems.getQuantity()); // remove what's in the warehouse
                this.warehouseDataService.createOrUpdateWarehouse(warehouse);
            } catch (WarehouseException e) {
                e.printStackTrace();
            }
        });
        // delete the warehouse items after we update each warehouse
        this.deleteWarehouseItems(warehouseItemsList);
        // delete the item
        this.itemDataService.deleteItem(itemId);
    }

    /**
     * Deleting a warehouse should return the items back to the item with no warehouse
     * @param warehouseId: id of the warehouse to delete
     */
    public void deleteWarehouse(long warehouseId) throws WarehouseException {
        List<WarehouseItems> warehouseItemsList = this.findAllWarehouses(warehouseId);

        warehouseItemsList.forEach(warehouseItems -> {
            // want to update item. Want to also catch exception because we want to move onto next warehouse even if the item does not exist
            try {
                Item item = this.itemDataService.getItem(warehouseItems.getItemId());
                // add the deleting items from the warehouse back to quantityNotInWarehouse for this item
                item.setQuantityNotInWarehouse(item.getQuantityNotInWarehouse() + warehouseItems.getQuantity());
                this.itemDataService.createOrUpdateItem(item, item.getTotalQuantity());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // before deleting we want to remove all items from it, so they don't get deleted as well.
        Warehouse warehouse = this.warehouseDataService.getWarehouse(warehouseId);
        // delete the warehouse items after we update each item b/c the relationship is no longer valid
        this.deleteWarehouseItems(warehouseItemsList);
        // delete the item
        this.warehouseDataService.deleteWarehouse(warehouseId);
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

    public void deleteWarehouseItems(List<WarehouseItems> warehouseItemsList) {
        this.warehouseItemsRepository.deleteAll(warehouseItemsList);
    }

    public List<WarehouseItems> findAllWarehouses(long warehouseId) {
        return this.warehouseItemsRepository.findAllByWarehouseId(warehouseId);
    }

    public List<WarehouseItems> findAllItems(long itemId) {
        return this.warehouseItemsRepository.findAllByItemId(itemId);
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

        canAddItemToWarehouse(warehouse, itemToSave, quantityToSave);


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

        // save entities to database
        this.warehouseDataService.createOrUpdateWarehouse(warehouse);
        this.itemDataService.createOrUpdateItem(itemToSave, itemToSave.getTotalQuantity());
        return this.warehouseItemsRepository.save(warehouseItems);
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

        // check if we can reduce the item from the warehouse
        canReduceItemFromWarehouse(warehouse, item, warehouseItems, quantityToDelete);


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
        this.itemDataService.createOrUpdateItem(item, item.getTotalQuantity());
        this.warehouseDataService.createOrUpdateWarehouse(warehouse);
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
    private void canAddItemToWarehouse(Warehouse warehouse, Item item, int quantityToAdd) throws AddItemToWarehouseException {

        int warehouseCapacityAfterAdd = warehouse.getCurrentCapacity() + quantityToAdd;
        int remainingItemQuantityAfterAdd = item.getQuantityNotInWarehouse() - quantityToAdd;

        if (warehouseCapacityAfterAdd > warehouse.getMaxCapacity()) {
            throw new AddItemToWarehouseException("Cannot add item to warehouse because warehouse will exceed max capacity: " +  warehouse.getMaxCapacity());
        } else if (remainingItemQuantityAfterAdd < 0) {
            throw new AddItemToWarehouseException("Cannot add item because item does not have enough quantity not in warehouse: " + item.getQuantityNotInWarehouse());
        } else if (quantityToAdd < 0) {
            throw new AddItemToWarehouseException("Quantity to add must be positive: " + quantityToAdd);
        }

    }


    private void canReduceItemFromWarehouse(Warehouse warehouse, Item item, WarehouseItems warehouseItems, int quantityToDelete) throws ReduceItemException {
        int warehouseCapacityAfterDelete = warehouse.getCurrentCapacity() - quantityToDelete;
        int itemNewQuantity = item.getQuantityNotInWarehouse() + quantityToDelete;
        int itemsInWarehouseAfterDelete = warehouseItems.getQuantity() - quantityToDelete;

        if (warehouseCapacityAfterDelete < 0) {
            throw new ReduceItemException("Cannot delete more items than warehouse currently has. Warehouse current capacity: " + warehouse.getCurrentCapacity());
        } else if (itemNewQuantity > item.getTotalQuantity()) {
            throw new ReduceItemException("Adding more items to the current item " + item.getDisplayName() + "than the total quantity of the item" + item.getTotalQuantity());
        } else if ( itemsInWarehouseAfterDelete < 0) {
            throw new ReduceItemException("Cannot delete more items that the warehouse has " + warehouseItems.getQuantity());
        }
    }

}
