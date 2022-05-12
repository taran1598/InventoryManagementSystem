package com.example.inventorymanagementsystem.warehouse;

import com.example.inventorymanagementsystem.item.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class WarehouseController {

    private final WarehouseDataService warehouseDataService;

    public WarehouseController(@Autowired WarehouseDataService warehouseDataService) {
        this.warehouseDataService = warehouseDataService;
    }


    @GetMapping("/warehouses")
    public List<Warehouse> getAllWarehouses() {
        return warehouseDataService.getAllWarehouses();
    }

    @GetMapping("/warehouses/{id}")
    public Warehouse getWarehouseById(@PathVariable long id) throws Exception {
        return this.warehouseDataService.getWarehouse(id);
    }

    @PostMapping("/warehouse")
    public Warehouse createOrUpdateWarehouse(@RequestBody Warehouse warehouse) {
        return this.warehouseDataService.createOrUpdateWarehouse(warehouse);
    }

    @PostMapping("/warehouses")
    public List<Warehouse> createOrUpdateWarehouse(@RequestBody List<Warehouse> warehouses) {
        return this.warehouseDataService.createOrUpdateWarehouse(warehouses);
    }

    @DeleteMapping("/warehouses/{id}")
    public void deleteWarehouse(@PathVariable long id) {
        this.warehouseDataService.deleteWarehouse(id);
    }

    @DeleteMapping("/warehouses")
    public void deleteWarehouses() {
        this.warehouseDataService.deleteAllWarehouses();
    }

    @PostMapping("/warehouses/addItem/{itemId}/warehouse/{warehouseId}")
    public Warehouse addItemToWarehouse(@PathVariable long itemId, @PathVariable long warehouseId) throws Exception {
        return this.warehouseDataService.addItemToWarehouse(itemId, warehouseId);
    }


}
