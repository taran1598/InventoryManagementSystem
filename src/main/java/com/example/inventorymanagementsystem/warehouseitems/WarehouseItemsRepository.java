package com.example.inventorymanagementsystem.warehouseitems;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WarehouseItemsRepository extends JpaRepository<WarehouseItems, WarehouseItemsCompositeKey>  {

    List<WarehouseItems> findAllByWarehouseId(long warehouseId);
}
