package com.example.inventorymanagementsystem.warehouseitems;

import com.example.inventorymanagementsystem.item.Item;
import com.example.inventorymanagementsystem.thymeleafAttributes.ThymeleafAttributes;
import com.example.inventorymanagementsystem.warehouse.Warehouse;
import com.example.inventorymanagementsystem.warehouse.WarehouseDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin
@Controller
public class WarehouseItemsController {

    private final WarehouseItemsDataService warehouseItemsDataService;
    private final WarehouseDataService warehouseDataService;

    public WarehouseItemsController(@Autowired WarehouseItemsDataService warehouseItemsDataService,
                                    @Autowired WarehouseDataService warehouseDataService) {
        this.warehouseItemsDataService = warehouseItemsDataService;
        this.warehouseDataService = warehouseDataService;
    }


    @PostMapping("/items/{itemId}/warehouse/{warehouseId}")
    public String saveItemToWarehouse(int quantity, @PathVariable long itemId, @PathVariable long warehouseId, Model model) {
        try {
            this.warehouseItemsDataService.saveTimeToWarehouse(itemId, warehouseId, quantity);
            return "redirect:/items";
        } catch (Exception e) {
            model.addAttribute(ThymeleafAttributes.error.toString(), e.getMessage());
            return "errorPage";
        }
    }

    @PostMapping("/reduceItem/{itemId}/warehouse/{warehouseId}")
    public String reduceItemInWarehouse(int quantityToReduce, @PathVariable long itemId, @PathVariable long warehouseId, Model model) {
        try {
            this.warehouseItemsDataService.reduceItemFromWarehouse(itemId, warehouseId, quantityToReduce);
            return "redirect:/warehouses";
        } catch (Exception e) {
            model.addAttribute(ThymeleafAttributes.error.toString(), e.getMessage());
            return "errorPage";
        }
    }

    @GetMapping("/itemsInWarehousePage/{id}")
    public String goToItemsInWarehousePage(@PathVariable long id, Model model) {

        try {
            Warehouse warehouse = this.warehouseDataService.getWarehouse(id);
            List<WarehouseItems> warehouseItemsList = this.warehouseItemsDataService.findAllWarehouses(id);

            List<Item> items = this.warehouseItemsDataService.getItemsInWarehouse(warehouseItemsList);

            // turn warehouseItemsList to map for quick retrieval in front-end
            Map<Long, Integer> itemIdToWarehouseQuantityMap =
                    warehouseItemsList.stream().collect(Collectors.toMap(WarehouseItems::getItemId, WarehouseItems::getQuantity));

            model.addAttribute("warehouse", warehouse);
            model.addAttribute("items", items);
            model.addAttribute("warehouseItemsMap", itemIdToWarehouseQuantityMap);
            return "itemsInWarehouse";
        } catch (Exception e) {
            model.addAttribute(ThymeleafAttributes.error.toString(), e.getMessage());
            return "errorPage";
        }
    }

    //                      //
    //   REDIRECTION URLS   //
    //                      //

    @GetMapping("/reduceItemsForm/{itemId}/{warehouseId}")
    public String goToReduceWarehouseItemsPage(@PathVariable long itemId, @PathVariable long warehouseId, Model model) {
        model.addAttribute("itemId", itemId);
        model.addAttribute("warehouseId", warehouseId);
        return "reduceWarehouseItemForm";
    }
}
