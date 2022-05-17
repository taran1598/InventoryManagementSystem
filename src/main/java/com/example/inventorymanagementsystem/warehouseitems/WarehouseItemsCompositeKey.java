package com.example.inventorymanagementsystem.warehouseitems;


import java.io.Serializable;
import java.util.Objects;

public class WarehouseItemsCompositeKey implements Serializable {

    private long itemId;
    private long warehouseId;

    public WarehouseItemsCompositeKey() {}

    public WarehouseItemsCompositeKey(long itemId, long warehouseId) {
        this.itemId = itemId;
        this.warehouseId = warehouseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WarehouseItemsCompositeKey)) return false;
        WarehouseItemsCompositeKey that = (WarehouseItemsCompositeKey) o;
        return itemId == that.itemId && warehouseId == that.warehouseId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, warehouseId);
    }
}
