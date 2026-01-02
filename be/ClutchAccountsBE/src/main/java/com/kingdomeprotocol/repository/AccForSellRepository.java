package com.kingdomeprotocol.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kingdomeprotocol.model.AccForSellModel;

import jakarta.persistence.LockModeType;

public interface AccForSellRepository extends JpaRepository<AccForSellModel, Integer>{
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query(value = "select acc from AccForSellModel acc where acc.account_id = :id")
	Optional<AccForSellModel> findAccbyId4Lock(@Param("id") int id);
	
	@Query(value = "select count(*) from AccForSell where account_type = :acc_type and isSold = 0", nativeQuery = true)
	int countAccLeft(@Param("acc_type") String accType);
	
	@Query(value = "select * from AccForSell where account_type = :acc_type and isSold = 0 order by account_id offset :skip_col rows fetch next 1 rows only", nativeQuery = true)
	Optional<AccForSellModel> randomAccByType(@Param("acc_type") String accType, @Param("skip_col") int skip_col);
	
	@Query(value = "select * from AccForSell where account_type = :type",nativeQuery = true)
	List<customInfoAcc> getInfoAcc(@Param("type") String type);
	interface customInfoAcc{
		Integer getAccount_id();
		Integer getPrice();
		String getAccount_type();
	}
}
