package com.mooninho.ordermanager.store.domain.entity;

import com.mooninho.ordermanager.owner.domain.entity.Owner;
import com.mooninho.ordermanager.menu.domain.entity.Menu;
import com.mooninho.ordermanager.order.domain.entity.Order;
import com.mooninho.ordermanager.store.domain.vo.StoreName;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;

    @Embedded
    @Column(nullable = false, unique = true)
    private StoreName storeName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<Order> order = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<Menu> menu = new ArrayList<>();

    private Store(Long id) {
        this.id = id;
    }

    private Store(StoreName storeName, Owner owner) {
        this.storeName = storeName;
        this.owner = owner;
    }

    public static Store createKeyObject(Long storeId) {
        return new Store(storeId);
    }

    public static Store createStore(StoreName storeName, Owner owner) {
        return new Store(storeName, owner);
    }
}
