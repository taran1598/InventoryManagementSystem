package com.example.inventorymanagementsystem.warehouse;

import com.example.inventorymanagementsystem.item.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@Controller
public class WarehouseController {

    private final WarehouseDataService warehouseDataService;

    public WarehouseController(@Autowired WarehouseDataService warehouseDataService) {
        this.warehouseDataService = warehouseDataService;
    }


    @GetMapping("/warehouses")
    public String getAllWarehouses(Model model) {
        model.addAttribute("warehouses",warehouseDataService.getAllWarehouses());
        return "warehouse.html";
    }

    @GetMapping("/warehouses/{id}")
    public Warehouse getWarehouseById(@PathVariable long id) throws Exception {
        return this.warehouseDataService.getWarehouse(id);
    }

    @PostMapping("/warehouse")
    public String createOrUpdateWarehouse(Warehouse warehouse) {
        this.warehouseDataService.createOrUpdateWarehouse(warehouse);
        return "redirect:/warehouses";
    }

//    @PostMapping("/item")
//    public String saveItem(Item item) {
//        this.itemService.createOrUpdateItem(item);
//
//        return "redirect:/items";
//    }

    @PostMapping("/warehouses")
    public List<Warehouse> createOrUpdateWarehouse(@RequestBody List<Warehouse> warehouses) {
        return this.warehouseDataService.createOrUpdateWarehouse(warehouses);
    }

    @DeleteMapping("/warehouses/{id}")
    public String deleteWarehouse(@PathVariable long id) {
        this.warehouseDataService.deleteWarehouse(id);
        return "redirect:/warehouses";
    }

    @DeleteMapping("/warehouses")
    public void deleteWarehouses() {
        this.warehouseDataService.deleteAllWarehouses();
    }

    @PostMapping("/warehouses/addItem/{itemId}/warehouse/{warehouseId}")
    public Warehouse addItemToWarehouse(@PathVariable long itemId, @PathVariable long warehouseId) throws Exception {
        return this.warehouseDataService.addItemToWarehouse(itemId, warehouseId);
    }

    // redirect endpoints

    @GetMapping("/addWarehousePage")
    public String goToAddWarehousesPage(Model model) {
        Warehouse newWarehouse = new Warehouse();
        model.addAttribute("warehouse", newWarehouse);
        return "addWarehouseForm";
    }

    @GetMapping("/editWarehousePage/{id}")
    public String goToEditItemsPage(@PathVariable long id, Model model) throws Exception {
        Warehouse warehouse = this.warehouseDataService.getWarehouse(id);
        model.addAttribute("warehouse", warehouse);
        return "addWarehouseForm";
    }

//    @GetMapping("/showWarehouseItemsPage/{id}")
//    public String goToShowWarehouseItemsPage(@PathVariable long id, Model model) throws Exception {
//        Warehouse warehouse = this.warehouseDataService.getWarehouse(id);
//    }


}
