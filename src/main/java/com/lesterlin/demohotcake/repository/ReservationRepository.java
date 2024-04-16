package com.lesterlin.demohotcake.repository;

import com.lesterlin.demohotcake.entity.Reservations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservations,Integer> {

    @Query("SELECT r.id, r.phoneNumber, r.startTime, r.endTime " +
            "FROM Reservations r " + // 添加别名 'r'
            "WHERE r.id = :storeId AND " +
            "(FUNCTION('YEAR', r.startTime) * 100 + FUNCTION('MONTH', r.startTime) = :yearAndMonth OR " +
            "FUNCTION('YEAR', r.endTime) * 100 + FUNCTION('MONTH', r.endTime) = :yearAndMonth)")
    List<Object[]> findReservationDetailsByStoreIdAndMonth(@Param("storeId") Integer storeId,
                                                           @Param("yearAndMonth") Integer yearAndMonth);

}
