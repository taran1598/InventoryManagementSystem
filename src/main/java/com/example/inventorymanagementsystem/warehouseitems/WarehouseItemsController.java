package com.example.inventorymanagementsystem.warehouseitems;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@CrossOrigin
@Controller
public class WarehouseItemsController {

    private final WarehouseItemsDataService warehouseItemsDataService;

    public WarehouseItemsController(@Autowired WarehouseItemsDataService warehouseItemsDataService) {
        this.warehouseItemsDataService = warehouseItemsDataService;
    }


    @PostMapping("/items/{itemId}/warehouse/{warehouseId}")
    public String saveItemToWarehouse(int quantity, @PathVariable long itemId, @PathVariable long warehouseId) throws Exception {
        this.warehouseItemsDataService.saveTimeToWarehouse(itemId, warehouseId, quantity);
        return "redirect:/items";
    }
}
