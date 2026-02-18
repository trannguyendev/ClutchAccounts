package com.kingdomeprotocol.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kingdomeprotocol.model.UserModel;
import com.kingdomeprotocol.model.Vouchers;
import com.kingdomeprotocol.model.VoucherusageModel;

@Repository
public interface VoucherusageRepository extends JpaRepository<VoucherusageModel, Integer>{
	boolean existsByUserAndVoucher(UserModel user, Vouchers voucher);
}
