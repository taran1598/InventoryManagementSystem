package com.example.inventorymanagementsystem.warehouseitems;

import com.example.inventorymanagementsystem.warehouse.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseItemsRepository extends JpaRepository<WarehouseItems, WarehouseItemsCompositeKey>  {
}
