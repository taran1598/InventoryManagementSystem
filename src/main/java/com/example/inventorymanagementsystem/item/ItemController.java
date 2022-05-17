package com.example.inventorymanagementsystem.item;

import com.example.inventorymanagementsystem.warehouse.WarehouseDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@Controller
public class ItemController {

    private final ItemDataService itemService;
    private final WarehouseDataService warehouseDataService;

    public ItemController(@Autowired ItemDataService itemService,
                          @Autowired WarehouseDataService warehouseDataService) {
        this.itemService = itemService;
        this.warehouseDataService = warehouseDataService;
    }

    @GetMapping("/items")
    public String getItems(Model model) {
        model.addAttribute("items", this.itemService.getItems());
        return "item.html";
    }

    @GetMapping("/items/{id}")
    public Item getItem(@PathVariable long id) throws Exception {
        return this.itemService.getItem(id);
    }

    @PostMapping("/item")
    public String saveItem(Item item) {
        this.itemService.createOrUpdateItem(item);
        return "redirect:/items";
    }

    @PostMapping("/items")
    public List<Item> saveAllItems(@RequestBody List<Item> items) {
        return this.itemService.createOrUpdateItems(items);
    }

    @DeleteMapping("/items")
    public void deleteAllItems() {
        this.itemService.deleteItems();
    }

    @DeleteMapping("/items/{id}")
    public String deleteItem(@PathVariable long id) {
        this.itemService.deleteItem(id);
        return "redirect:/items";
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
    public String goToEditItemsPage(@PathVariable long id, Model model) throws Exception {
        Item item = this.itemService.getItem(id);
        model.addAttribute("item", item);
        return "addItemForm";
    }

    @GetMapping("/addItemToWarehousePage/{id}")
    public String goToAddItemTOwarehousePage(@PathVariable long id, Model model) throws Exception {
        Item item = this.itemService.getItem(id);
        model.addAttribute("item", item);
        model.addAttribute("warehouses", warehouseDataService.getAllWarehouses());
        return "addItemToWarehouse";
    }

    @GetMapping("/goToWarehouseQuantityForm/{itemId}/{warehouseId}")
    public String goToAddItemTOwarehousePage(@PathVariable long itemId, @PathVariable long warehouseId, Model model) throws Exception {
        model.addAttribute("itemId", itemId);
        model.addAttribute("warehouseId", warehouseId);
        return "warehouseItemQuantityForm";
    }


}
