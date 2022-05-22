package com.example.inventorymanagementsystem.warehouse;


import static org.assertj.core.api.Assertions.assertThat;

import com.example.inventorymanagementsystem.InventoryManagementSystemApplication;
import com.example.inventorymanagementsystem.exceptions.WarehouseException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.fail;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= InventoryManagementSystemApplication.class)
public class WarehouseDataServiceTests {

    @Autowired
    private WarehouseDataService warehouseDataService;

    @Test
    public void shouldSaveWarehouseWithOnlyWarehouseNameMaxCapacityAddress() {

        try {
            String warehouseName = "testWarehouseName";
            int maxCapacity = 30;
            String address = "Vancouver, BC";
            Warehouse warehouse = new Warehouse(warehouseName, maxCapacity, address);
            Warehouse savedWarehouse = this.warehouseDataService.createOrUpdateWarehouse(warehouse);
            assertThat(savedWarehouse.getWarehouseId()).isNotNull();
            assertThat(savedWarehouse.getWarehouseName()).isEqualTo(warehouseName);
            assertThat(savedWarehouse.getMaxCapacity()).isEqualTo(maxCapacity);
            assertThat(savedWarehouse.getAddress()).isEqualTo(address);
            assertThat(savedWarehouse.getCurrentCapacity()).isEqualTo(0);
        } catch (Exception e) {
            fail("Should not have thrown exception when saving warehouse with name, max capacity and address");
        }
    }

    @Test
    public void shouldSaveMultipleWarehouses() {

        try {
            String warehouseName = "testWarehouseName";
            int maxCapacity = 30;
            String address = "Vancouver, BC";
            String warehouseNameTwo = "testWarehouseNameTwo";
            int maxCapacityTwo = 1;
            String addressTwo = "Vancouver, BC Two";
            List<Warehouse> warehouses = Arrays.asList(new Warehouse(warehouseName, maxCapacity, address),
                                                       new Warehouse(warehouseNameTwo, maxCapacityTwo, addressTwo));

            List<Warehouse> savedWarehouses = this.warehouseDataService.createOrUpdateWarehouse(warehouses);

            assertThat(savedWarehouses.size()).isEqualTo(2);
        } catch (Exception e) {
            fail("Should not have thrown exception when saving warehouse with name, max capacity and address");
        }
    }

    @Test
    public void shouldDeleteWarehouse() {
        try {
            String warehouseName = "testWarehouseName";
            int maxCapacity = 30;
            String address = "Vancouver, BC";
            Warehouse warehouse = new Warehouse(warehouseName, maxCapacity, address);
            Warehouse savedWarehouse = this.warehouseDataService.createOrUpdateWarehouse(warehouse);
            this.warehouseDataService.deleteWarehouse(savedWarehouse.getWarehouseId());

            try {
                Warehouse getWarehouse = this.warehouseDataService.getWarehouse(savedWarehouse.getWarehouseId());
                fail("Should not have retrieved warehouse");
            } catch (Exception e) {
                assertThat(e).isInstanceOf(WarehouseException.class);
            }

        } catch (Exception e) {
           fail("Deleting warehouse should have passed");
        }
    }

    @Test
    public void shouldNotDeleteWarehouseThatDoesNotExist() {
        try {

            this.warehouseDataService.deleteWarehouse(ThreadLocalRandom.current().nextInt(23, 12311));
            fail("Deleting warehouse that doesn't exist should not have passed");
        } catch (Exception e) {
            assertThat(e).isInstanceOf(WarehouseException.class);
        }
    }

}
