package com.example.inventorymanagementsystem.item;


import com.example.inventorymanagementsystem.warehouse.Warehouse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name="Item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "displayName", nullable = false)
    private String displayName;

    @Column(name = "quantity")
    private int quantity;

    @CreationTimestamp
    @Column(name = "createdTimeStamp", nullable = false)
    private Instant createdTimeStamp;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "warehouse_fk", referencedColumnName = "warehouse_id", nullable = true)
    private Warehouse warehouse;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return getId().equals(item.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
