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
    private ReservationRepository reservationRepository;

    @Autowired
    private StoreRepository storeRepository;

    @PostMapping
    public ResponseEntity<String> createReservation(@Valid @RequestBody Reservations reservation) {
        // 查詢 storeRepository 中是否存在與 reservation 中的 storeId 對應的商店
        Optional<Stores> storeOptional = storeRepository.findById(reservation.getStoreId());
        // 檢查是否找到對應的商店
        if (storeOptional.isPresent()) {
            Stores store = storeOptional.get();
            // 如果找到商店就檢查預約時間是否在商店的營業時間內
            if(reservation.getStartTime().isBefore(store.getOpeningTime()) ||
                    reservation.getEndTime().isAfter(store.getClosingTime())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("預約失敗，預約時間不在商店營業時間內");
            }
            // 如果找到就保存此預約到資料庫
            reservationRepository.save(reservation);
            return ResponseEntity.ok("預約成功");
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("預約失敗，商店不存在");
        }
    }

    @GetMapping("/{storeId}/{yearAndMonth}")
    public ResponseEntity<List<Object[]>> getReservationsByStoreIdAndMonth(@PathVariable Integer storeId,
                                                                           @PathVariable String yearAndMonth) {
        int year = Integer.parseInt(yearAndMonth.substring(0, 4));
        int month = Integer.parseInt(yearAndMonth.substring(4));
        int yearMonth = year * 100 + month;
        List<Object[]> reservations = reservationRepository.findReservationDetailsByStoreIdAndMonth(storeId, yearMonth);
        if (reservations.isEmpty()) {
            return ResponseEntity.noContent().build(); // 沒有符合的資料，返回204狀態碼(無內容)
        } else {
            return ResponseEntity.ok(reservations); // 返回200狀態碼並返回資料
        }
    }

}
