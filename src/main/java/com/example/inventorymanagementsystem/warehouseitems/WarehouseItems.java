package com.example.inventorymanagementsystem.warehouseitems;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity @IdClass(WarehouseItemsCompositeKey.class)
@Table(name="WarehouseItems")
public class WarehouseItems {

    @Id
    private long itemId;
    @Id
    private long warehouseId;

    private int quantity;

    public WarehouseItems(long itemId, long warehouseId, int quantity) {
        this.itemId = itemId;
        this.warehouseId = warehouseId;
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WarehouseItems)) return false;
        WarehouseItems that = (WarehouseItems) o;
        return getItemId() == that.getItemId() && getWarehouseId() == that.getWarehouseId() && getQuantity() == that.getQuantity();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getItemId(), getWarehouseId(), getQuantity());
    }
}
