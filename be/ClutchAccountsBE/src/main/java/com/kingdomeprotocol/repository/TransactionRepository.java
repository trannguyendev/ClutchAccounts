package com.kingdomeprotocol.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kingdomeprotocol.model.TransactionsModel;

public interface TransactionRepository extends JpaRepository<TransactionsModel, Integer>{

	@Query(value = "select * from Transactions where user_id = :id", nativeQuery = true)
	List<paymentLog> getSelfPaymentLog(@Param("id") int id);
	interface paymentLog{
		Integer getId();
		Integer getUser_id();
		Integer getAmount();
		String getType();
		String getDescrp();
		LocalDateTime getCreated_at();
	}
}
