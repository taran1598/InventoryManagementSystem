package com.example.inventorymanagementsystem.warehouseitems;

import com.example.inventorymanagementsystem.item.Item;
import com.example.inventorymanagementsystem.item.ItemDataService;
import com.example.inventorymanagementsystem.warehouse.Warehouse;
import com.example.inventorymanagementsystem.warehouse.WarehouseDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
            throw new Exception("Cannot add item to warehouse");
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
     * @param warehouse
     * @param item
     * @return
     */
    private boolean canAddItemToWarehouse(Warehouse warehouse, Item item, int quantityToAdd) {

        int warehouseCapacityAfterAdd = warehouse.getCurrentCapacity() + quantityToAdd;
        int remainingItemQuantityAfterAdd = item.getQuantityNotInWarehouse() - quantityToAdd;

        return warehouseCapacityAfterAdd <= warehouse.getMaxCapacity() && remainingItemQuantityAfterAdd >= 0;


    }


}
