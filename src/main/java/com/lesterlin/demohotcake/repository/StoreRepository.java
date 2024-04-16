package com.lesterlin.demohotcake.repository;

import com.lesterlin.demohotcake.entity.Stores;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Stores, Integer> {
    Stores findByName(String storeName);
}
