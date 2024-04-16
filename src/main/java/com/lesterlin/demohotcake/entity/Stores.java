package com.lesterlin.demohotcake.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Stores {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer storeId;
    @NotEmpty
    private String storeName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime openingTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime closingTime;
}
