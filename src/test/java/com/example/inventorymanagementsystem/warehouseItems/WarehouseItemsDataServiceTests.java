package com.example.inventorymanagementsystem.warehouseItems;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import com.example.inventorymanagementsystem.InventoryManagementSystemApplication;
import com.example.inventorymanagementsystem.item.Item;
import com.example.inventorymanagementsystem.item.ItemDataService;
import com.example.inventorymanagementsystem.warehouse.Warehouse;
import com.example.inventorymanagementsystem.warehouse.WarehouseDataService;
import com.example.inventorymanagementsystem.warehouseitems.WarehouseItems;
import com.example.inventorymanagementsystem.warehouseitems.WarehouseItemsCompositeKey;
import com.example.inventorymanagementsystem.warehouseitems.WarehouseItemsController;
import com.example.inventorymanagementsystem.warehouseitems.WarehouseItemsDataService;
import org.junit.jupiter.api.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= InventoryManagementSystemApplication.class)
public class WarehouseItemsDataServiceTests {

    @Autowired
    private WarehouseItemsDataService warehouseItemsDataService;
    @Autowired
    private ItemDataService itemDataService;
    @Autowired
    private WarehouseDataService warehouseDataService;


    @Test
    public void addItemToWarehouseTest() {
        try {
        int totalQuantity = 20;
        Item item = itemDataService.createOrUpdateItem(new Item("testItem", totalQuantity), totalQuantity);
        Warehouse warehouse = warehouseDataService.createOrUpdateWarehouse(new Warehouse("testWarehouse", 20, "testAddress"));
        int quantityOfItemToAddToWarehouse = 19;



            WarehouseItems savedWarehouseItems = warehouseItemsDataService.saveTimeToWarehouse(
                    item.getId(),
                    warehouse.getWarehouseId(),
                    quantityOfItemToAddToWarehouse
            );
            assertThat(savedWarehouseItems.getWarehouseId()).isEqualTo(warehouse.getWarehouseId());
            assertThat(savedWarehouseItems.getItemId()).isEqualTo(item.getId());
            assertThat(savedWarehouseItems.getQuantity()).isEqualTo(quantityOfItemToAddToWarehouse);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void reduceItemFromWarehouseShouldHaveCapacityAtOneTest() {
        try {
        int totalQuantity = 20;
        Item item = itemDataService.createOrUpdateItem(new Item("testItem", totalQuantity), totalQuantity);
        Warehouse warehouse = warehouseDataService.createOrUpdateWarehouse(new Warehouse("testWarehouse", 20, "testAddress"));
        WarehouseItemsCompositeKey key = new WarehouseItemsCompositeKey(item.getId(), warehouse.getWarehouseId());
        int quantityOfItemToAddToWarehouse = 19;
        int quantityOfItemToReduce = 18;

            WarehouseItems savedWarehouseItems = warehouseItemsDataService.saveTimeToWarehouse(
                    item.getId(),
                    warehouse.getWarehouseId(),
                    quantityOfItemToAddToWarehouse
            );
            assertThat(savedWarehouseItems.getWarehouseId()).isEqualTo(warehouse.getWarehouseId());
            assertThat(savedWarehouseItems.getItemId()).isEqualTo(item.getId());
            assertThat(savedWarehouseItems.getQuantity()).isEqualTo(quantityOfItemToAddToWarehouse);

            // remove 18 quantity from warehouse
            warehouseItemsDataService.reduceItemFromWarehouse(item.getId(), warehouse.getWarehouseId(), quantityOfItemToReduce);
            WarehouseItems warehouseItemsAfterReduce = warehouseItemsDataService.getWarehouseItem(key);

            assertThat(warehouseItemsAfterReduce.getQuantity()).isEqualTo(1);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void reduceItemFromWarehouseShouldDeleteItemTest() {
        try {
        int totalQuantity = 20;
        Item item = itemDataService.createOrUpdateItem(new Item("testItem", totalQuantity), totalQuantity);
        Warehouse warehouse = warehouseDataService.createOrUpdateWarehouse(new Warehouse("testWarehouse", 20, "testAddress"));
        WarehouseItemsCompositeKey key = new WarehouseItemsCompositeKey(item.getId(), warehouse.getWarehouseId());
        int quantityOfItemToAddToWarehouse = 19;
        int quantityOfItemToReduce = 19;


            WarehouseItems savedWarehouseItems = warehouseItemsDataService.saveTimeToWarehouse(
                    item.getId(),
                    warehouse.getWarehouseId(),
                    quantityOfItemToAddToWarehouse
            );
            assertThat(savedWarehouseItems.getWarehouseId()).isEqualTo(warehouse.getWarehouseId());
            assertThat(savedWarehouseItems.getItemId()).isEqualTo(item.getId());
            assertThat(savedWarehouseItems.getQuantity()).isEqualTo(quantityOfItemToAddToWarehouse);

            // remove 18 quantity from warehouse
            warehouseItemsDataService.reduceItemFromWarehouse(item.getId(), warehouse.getWarehouseId(), quantityOfItemToReduce);
            try {
                WarehouseItems warehouseItemsAfterReduce = warehouseItemsDataService.getWarehouseItem(key);
                fail("The item should have been deleted from warehouse");
            } catch (Exception e) {
                // should pass
            }


        } catch (Exception e) {
            fail(e.getMessage());
        }

    }
}
