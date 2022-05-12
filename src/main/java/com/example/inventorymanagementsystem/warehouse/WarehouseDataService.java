package com.example.inventorymanagementsystem.warehouse;

import com.example.inventorymanagementsystem.item.Item;
import com.example.inventorymanagementsystem.item.ItemDataService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WarehouseDataService {


    private final WarehouseRepository warehouseRepository;
    private final ItemDataService itemDataService;

    public WarehouseDataService(@Autowired WarehouseRepository warehouseRepository,
                                @Autowired ItemDataService itemDataService) {
        this.warehouseRepository = warehouseRepository;
        this.itemDataService = itemDataService;
    }

    public List<Warehouse> getAllWarehouses() {
        return this.warehouseRepository.findAll();
    }

    public Warehouse getWarehouse(long id) throws Exception {
        return this.warehouseRepository.findById(id)
                .orElseThrow(() -> new Exception("No Warehouse found by id: " + id));
    }

    public Warehouse createOrUpdateWarehouse(Warehouse warehouse) {
        return this.warehouseRepository.save(warehouse);
    }

    public List<Warehouse> createOrUpdateWarehouse(List<Warehouse> warehouses) {
        return this.warehouseRepository.saveAll(warehouses);
    }

    public void deleteWarehouse(Warehouse warehouse) {
        this.warehouseRepository.delete(warehouse);
    }

    public void deleteWarehouse(long id) {
        this.warehouseRepository.deleteById(id);
    }

    public void deleteAllWarehouses() {
        this.warehouseRepository.deleteAll();
    }

    @Transactional
    public Warehouse addItemToWarehouse(long itemId, long warehouseId) throws Exception {
        Item item = this.itemDataService.getItem(itemId);
        Warehouse warehouseToAddItemTo = this.getWarehouse(warehouseId);
        warehouseToAddItemTo.addItem(item);
        item.setWarehouse(warehouseToAddItemTo);
        this.itemDataService.createOrUpdateItem(item);
        return this.warehouseRepository.save(warehouseToAddItemTo);
    }



}
