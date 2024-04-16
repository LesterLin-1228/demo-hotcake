package com.lesterlin.demohotcake.controller;

import com.lesterlin.demohotcake.entity.Stores;
import com.lesterlin.demohotcake.repository.StoreRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stores")
public class StoreController {

    @Autowired
    private StoreRepository storeRepository;

    @PostMapping
    public ResponseEntity<String> createStore(@Valid @RequestBody Stores store) {
        // 根據商店名稱查詢該商店是否存在
        Stores existingStore = storeRepository.findByName(store.getName());
        // 若不存在就傳入店名、開店時間、關店時間，創建商店
        if (existingStore == null) {
            storeRepository.save(store);
            return ResponseEntity.ok("創建商店成功");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("商店名稱重複");
        }
    }
}
