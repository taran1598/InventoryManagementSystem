package com.example.inventorymanagementsystem.warehouse;

import com.example.inventorymanagementsystem.item.Item;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.*;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name="Warehouse")
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    @Column(name = "warehouse_name")
    private String warehouseName;

    @Column(name = "maxCapacity")
    private int maxCapacity;

    @Column(name = "currentCapacity")
    private int currentCapacity;

    @Column(name = "address", nullable = false)
    private String address;

//    @ManyToMany(fetch = FetchType.LAZY ,mappedBy = "warehouse", targetEntity = Item.class, cascade = CascadeType.ALL)
//    @Column(name = "items", nullable = true)
//    @ToString.Exclude
//    private Set<Item> items = new HashSet<>();

    public Warehouse(String warehouseName, int maxCapacity, String address) {
        this.warehouseName = warehouseName;
        this.maxCapacity = maxCapacity;
        this.address = address;
    }

//    public void addItem(Item item) {
//        this.getItems().add(item);
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Warehouse)) return false;
        Warehouse warehouse = (Warehouse) o;
        return getWarehouseId().equals(warehouse.getWarehouseId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getWarehouseId());
    }
}
