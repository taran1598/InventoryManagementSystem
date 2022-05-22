package com.example.inventorymanagementsystem.warehouse;

import com.example.inventorymanagementsystem.exceptions.WarehouseException;
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

    public WarehouseDataService(@Autowired WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;

    }

    public List<Warehouse> getAllWarehouses() {
        return this.warehouseRepository.findAll();
    }

    public Warehouse getWarehouse(long id) throws WarehouseException {
        return this.warehouseRepository.findById(id)
                .orElseThrow(() -> new WarehouseException("No Warehouse found by id: " + id));
    }

    public Warehouse createOrUpdateWarehouse(Warehouse warehouse) {
        return this.warehouseRepository.save(warehouse);
    }

    public List<Warehouse> createOrUpdateWarehouse(List<Warehouse> warehouses) {
        return this.warehouseRepository.saveAll(warehouses);
    }

    public void deleteWarehouse(long id) throws WarehouseException {
        try {
            this.warehouseRepository.deleteById(id);
        } catch (Exception e) {
            throw new WarehouseException(e.getMessage());
        }
    }

    public void deleteAllWarehouses() {
        this.warehouseRepository.deleteAll();
    }



}
