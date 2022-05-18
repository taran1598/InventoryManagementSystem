package com.example.inventorymanagementsystem.warehouse;

import com.example.inventorymanagementsystem.item.Item;
import com.example.inventorymanagementsystem.thymeleafAttributes.ThymeleafAttributes;
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
    public String getWarehouseById(@PathVariable long id, Model model) {
        try {
            model.addAttribute("warehouse", this.warehouseDataService.getWarehouse(id));
            return "";
        } catch (Exception e) {
            model.addAttribute(ThymeleafAttributes.error.toString(), e.getMessage());
            return "errorPage";
        }
    }

    @PostMapping("/warehouse")
    public String createOrUpdateWarehouse(Warehouse warehouse) {
        this.warehouseDataService.createOrUpdateWarehouse(warehouse);
        return "redirect:/warehouses";
    }


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


    // redirect endpoints

    @GetMapping("/addWarehousePage")
    public String goToAddWarehousesPage(Model model) {
        Warehouse newWarehouse = new Warehouse();
        model.addAttribute("warehouse", newWarehouse);
        return "addWarehouseForm";
    }

    @GetMapping("/editWarehousePage/{id}")
    public String goToEditItemsPage(@PathVariable long id, Model model) {
        try {
            Warehouse warehouse = this.warehouseDataService.getWarehouse(id);
            model.addAttribute("warehouse", warehouse);
            return "addWarehouseForm";
        } catch (Exception e) {
            model.addAttribute(ThymeleafAttributes.error.toString(), e.getMessage());
            return "errorPage";
        }
    }



}
