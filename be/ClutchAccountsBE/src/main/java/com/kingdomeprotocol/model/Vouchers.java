package com.kingdomeprotocol.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    @Column(name = "voucher_code")
    private String voucherCode;

    @Column(name = "discount_percent")
    private int discountPercent;

    @Column(name = "max_usage")
    private int maxUsage;

    @Column(name = "used_count")
    private int usedCount;

    @Column(name = "exprired_at")
    private LocalDateTime expiryAt;

    @Column(name = "isActive")
    private boolean isActive;
}
