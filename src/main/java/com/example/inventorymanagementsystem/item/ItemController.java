package com.example.inventorymanagementsystem.item;

import com.example.inventorymanagementsystem.thymeleafAttributes.ThymeleafAttributes;
import com.example.inventorymanagementsystem.warehouse.WarehouseDataService;
import com.example.inventorymanagementsystem.warehouseitems.WarehouseItemsDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@Controller
public class ItemController {

    private final ItemDataService itemService;
    private final WarehouseDataService warehouseDataService;
    private final WarehouseItemsDataService warehouseItemsDataService;

    public ItemController(@Autowired ItemDataService itemService,
                          @Autowired WarehouseDataService warehouseDataService,
                          @Autowired WarehouseItemsDataService warehouseItemsDataService) {
        this.itemService = itemService;
        this.warehouseDataService = warehouseDataService;
        this.warehouseItemsDataService = warehouseItemsDataService;
    }

    @GetMapping("/items")
    public String getItems(Model model) {
        model.addAttribute("items", this.itemService.getItems());
        return "item.html";
    }

    @PostMapping("/item")
    public String saveItem(Item item, int oldTotalQuantity, Model model) {
        try {
            // update item not in warehouse
            this.itemService.createOrUpdateItem(item, oldTotalQuantity);
            return "redirect:/items";
        } catch (Exception e) {
            model.addAttribute(ThymeleafAttributes.error.toString(), e.getMessage());
            return "errorPage";
        }
    }

    @DeleteMapping("/items")
    public void deleteAllItems() {
        this.itemService.deleteItems();
    }

    @DeleteMapping("/items/{id}")
    public String deleteItem(@PathVariable long id, Model model) {
        try {
            this.warehouseItemsDataService.deleteItem(id);
            return "redirect:/items";
        } catch (Exception e) {
            model.addAttribute(ThymeleafAttributes.error.toString(), e.getMessage());
            return "errorPage";
        }
    }


    //                                   //
    // ITEM PAGE REDIRECTION ENDPOINTS   //
    //                                   //

    @GetMapping("/addItemsPage")
    public String goToAddItemsPage(Model model) {
        Item newItem = new Item();
        model.addAttribute("item", newItem);
        return "addItemForm";
    }

    @GetMapping("/editItemsPage/{id}")
    public String goToEditItemsPage(@PathVariable long id, Model model) {
        try {
            Item item = this.itemService.getItem(id);
            model.addAttribute("item", item);
            return "addItemForm";
        } catch (Exception e) {
            model.addAttribute(ThymeleafAttributes.error.toString(), e.getMessage());
            return "errorPage";
        }
    }

    @GetMapping("/addItemToWarehousePage/{id}")
    public String goToAddItemTOwarehousePage(@PathVariable long id, Model model) {
        try {
            Item item = this.itemService.getItem(id);
            model.addAttribute("item", item);
            model.addAttribute("warehouses", warehouseDataService.getAllWarehouses());
            return "addItemToWarehouse";
        } catch (Exception e) {
            model.addAttribute(ThymeleafAttributes.error.toString(), e.getMessage());
            return "errorPage";
        }
    }

    @GetMapping("/quantityToAdd/{itemId}/{warehouseId}")
    public String goToAddItemTOwarehousePage(@PathVariable long itemId, @PathVariable long warehouseId, Model model) {
        model.addAttribute("itemId", itemId);
        model.addAttribute("warehouseId", warehouseId);
        return "warehouseItemQuantityForm";
    }
}
