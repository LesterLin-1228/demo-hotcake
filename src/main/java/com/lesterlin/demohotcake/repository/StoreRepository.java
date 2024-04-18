package com.lesterlin.demohotcake.repository;

import com.lesterlin.demohotcake.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Store findByName(String storeName);
}
