package com.kingdomeprotocol.repository;

import java.time.LocalDateTime;
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
	@Query(value = "select * from AccForSell where account_type = :type and isSold = 0",nativeQuery = true)
	List<customInfoAcc> getInfoAcc(@Param("type") String type);
	@Query(value = "select top 5 * from AccForSell where account_type = :type and isSold = 0",nativeQuery = true)
	List<customInfoAcc> get5InfoAccFA(@Param("type") String type);
	interface customInfoAcc{
		Integer getAccount_id();
		Integer getPrice();
		String getAccount_type();
	}
	@Query(value = "select acc.account_id as account_id,\r\n"
			+ "acc.email as email,\r\n"
			+ "acc.username as username,\r\n"
			+ "acc.account_psw as password,\r\n"
			+ "acc.listed_at as listed_at,\r\n"
			+ "acc.isSold as isSold,\r\n"
			+ "acc.isLocked as isLocked,\r\n"
			+ "acc.lockedUntil as lockedUntil,\r\n"
			+ "acc.price as price,\r\n"
			+ "acc.account_type as account_type,\r\n"
			+ "img.image_url as image_url\r\n"
			+ "from AccForSell acc left join AccountImage img\r\n"
			+ "on acc.account_id = img.account_id order by acc.account_id desc", nativeQuery = true)
	List<customInfoAccAdmin> getAllCurrentAcc();
	interface customInfoAccAdmin{
		Integer getAccount_id();
		String getEmail();
		String getUsername();
		String getPassword();
		LocalDateTime getListed_at();
		boolean getIsSold();
		boolean getIsLocked();
		LocalDateTime getLockedUntil();
		Integer getPrice();
		String getAccount_type();
		String getImage_url();
	}
}
