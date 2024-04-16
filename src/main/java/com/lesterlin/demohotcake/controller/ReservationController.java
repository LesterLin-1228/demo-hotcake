package com.lesterlin.demohotcake.controller;

import com.lesterlin.demohotcake.entity.Reservations;
import com.lesterlin.demohotcake.entity.Stores;
import com.lesterlin.demohotcake.repository.ReservationRepository;
import com.lesterlin.demohotcake.repository.StoreRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("reservations")
public class ReservationController {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ReservationRepository reservationRepository;


    @PostMapping
    public ResponseEntity<String> createReservation(@Valid @RequestBody Reservations reservation) {
        // 查詢 storeRepository 中是否存在與 reservation 中的 storeId 對應的商店
        Stores store = storeRepository.findById(reservation.getStore().getId())
                .orElseThrow(() -> new IllegalArgumentException("Store not found"));
        // 如果找到商店就檢查預約時間是否在商店的營業時間內
        if (reservation.getStartTime().isBefore(store.getOpeningTime()) ||
                reservation.getEndTime().isAfter(store.getClosingTime())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("預約失敗，預約時間不在商店營業時間內");
        }
        // 如果都找到就保存此預約到資料庫
        reservationRepository.save(reservation);
        return ResponseEntity.ok("預約成功");

}

@GetMapping
public ResponseEntity<List<Object[]>> getReservationsByStoreIdAndMonth(
        @RequestParam(value = "storeId", required = true) Integer storeId,
        @RequestParam(value = "year", required = true) Integer year,
        @RequestParam(value = "month", required = true) Integer month) {

    int yearMonth = year * 100 + month;
    List<Object[]> reservations = reservationRepository.findReservationDetailsByStoreIdAndMonth(storeId, yearMonth);

    if (reservations.isEmpty()) {
        return ResponseEntity.noContent().build(); // 沒有符合的資料，返回204狀態碼(無內容)
    } else {
        return ResponseEntity.ok(reservations); // 返回200狀態碼並返回資料
    }
}

}
