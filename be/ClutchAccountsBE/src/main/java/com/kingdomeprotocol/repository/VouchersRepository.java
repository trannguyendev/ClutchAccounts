package com.kingdomeprotocol.repository;

import com.kingdomeprotocol.model.Vouchers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface VouchersRepository extends JpaRepository<Vouchers, Integer> {

    @Query("SELECT v FROM Vouchers v WHERE v.voucherCode LIKE %:voucherCode%")
    Vouchers getVoucherByVoucherCode(@Param("voucherCode") String voucherCode);
    
    @Modifying
    @Transactional
    @Query(value = "update Vouchers v set v.used_count = v.used_count + 1 where v.voucher_code = :code and v.used_count < v.max_usage and v.isActive = 1 and v.expired_at > CURRENT_TIMESTAMP", nativeQuery = true)
    int increaseUsedCountWhenValid(@Param("code") String code);
}