package com.kingdomeprotocol.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Vouchers")
public class Vouchers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voucher_id")
    private int id;

    @JsonProperty("voucher_code")
    @Column(name = "voucher_code")
    private String voucherCode;

    @JsonProperty("discount_percent")
    @Column(name = "discount_percent")
    private int discountPercent;

    @JsonProperty("max_usage")
    @Column(name = "max_usage")
    private int maxUsage;

    @JsonProperty("used_count")
    @Column(name = "used_count")
    private int usedCount;

    @JsonProperty("expired_at")
    @Column(name = "expired_at")
    private LocalDateTime expiryAt;

    @JsonProperty("isActive")
    @Column(name = "isActive")
    private boolean isActive;
    
    @JsonProperty("max_discount")
    @NotNull(message = "Pls do not leave this null")
    @Column(name = "max_discount")
    private int maxDiscount;
}