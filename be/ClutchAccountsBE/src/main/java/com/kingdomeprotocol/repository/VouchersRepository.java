package com.kingdomeprotocol.repository;

import com.kingdomeprotocol.model.Vouchers;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VouchersRepository extends JpaRepository<Vouchers, Integer> {

    @Query("SELECT v FROM Vouchers v WHERE v.voucherCode LIKE %:voucherCode%")
    List<Vouchers> getVoucherByVoucherCode(@Param("voucherCode") String voucherCode);
}