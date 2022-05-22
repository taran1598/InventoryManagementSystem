package com.example.inventorymanagementsystem.item;


import static org.assertj.core.api.Assertions.assertThat;

import com.example.inventorymanagementsystem.InventoryManagementSystemApplication;
import com.example.inventorymanagementsystem.exceptions.ItemException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= InventoryManagementSystemApplication.class)
public class ItemDataServiceTests {

    @Autowired
    private ItemDataService itemDataService;

    @Test
    public void shouldSaveItemWithNoIdToDatabase() {
        try {

            String displayName = "testItemName";
            int quantity = 20;

            Item item = new Item(displayName, quantity);
            Item savedItem = itemDataService.createOrUpdateItem(item, quantity);
            assertThat(savedItem.getId()).isNotNull();
            assertThat(savedItem.getCreatedTimeStamp()).isNotNull();
            assertThat(savedItem.getQuantityNotInWarehouse()).isEqualTo(quantity);
            assertThat(savedItem.getDisplayName()).isEqualTo(displayName);
            assertThat(savedItem.getTotalQuantity()).isEqualTo(quantity);
        } catch (Exception e) {
            fail("Should not have failed when saving item to db with no id");
        }

    }

    @Test
    public void shouldSaveMultipleItemsAndRetrieveMultipleItems() {

        try {
            String displayName = "testItemName";
            String displayName2 = "testItemName2";
            int quantity = 20;
            int quantity2 = 2;

            Item item = new Item(displayName, quantity);
            Item itemTwo = new Item(displayName2, quantity2);
            itemDataService.createOrUpdateItem(item, quantity);
            itemDataService.createOrUpdateItem(itemTwo, quantity2);

            List<Item> items = itemDataService.getItems();
            assertThat(items.size()).isEqualTo(2);
        } catch (Exception e) {
            fail("Should not have failed when saving multiple item to db");
        }
    }

    @Test
    public void shouldBeAbleToDeleteItem() {

        try {
            String displayName = "testItemName";
            int quantity = 20;

            Item item = new Item(displayName, quantity);
            itemDataService.createOrUpdateItem(item, quantity);

            itemDataService.deleteItem(item.getId());

            try {
                itemDataService.getItem(item.getId());
                fail("Should have thrown exception");
            } catch (ItemException e) {
                assertThat(e).isInstanceOf(ItemException.class);
            }
        } catch (Exception e) {
            fail("Should be able to delete an item");
        }
    }
}
