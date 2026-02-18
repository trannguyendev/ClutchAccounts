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
	@Query(value = "SELECT \r\n"
			+ "    acc.account_id,\r\n"
			+ "    acc.email,\r\n"
			+ "    acc.username,\r\n"
			+ "    acc.account_psw AS password,\r\n"
			+ "    acc.listed_at,\r\n"
			+ "    acc.isSold,\r\n"
			+ "    acc.isLocked,\r\n"
			+ "    acc.lockedUntil,\r\n"
			+ "    acc.price,\r\n"
			+ "    acc.account_type,\r\n"
			+ "    img.image_url,\r\n"
			+ "    sub.rank_info,\r\n"
			+ "    sub.vp,\r\n"
			+ "    sub.melee_amount,\r\n"
			+ "    sub.gun_amount,\r\n"
			+ "    sub.btp\r\n"
			+ "FROM AccForSell acc\r\n"
			+ "LEFT JOIN AccountImage img ON acc.account_id = img.account_id\r\n"
			+ "LEFT JOIN SubInfo sub ON acc.account_id = sub.account_id\r\n"
			+ "ORDER BY acc.account_id DESC", nativeQuery = true)
	List<customInfoAccAdmin> getAllCurrentAcc();
	@Query(value = "SELECT \r\n"
			+ "    acc.account_id,\r\n"
			+ "    acc.email,\r\n"
			+ "    acc.username,\r\n"
			+ "    acc.account_psw AS [password],\r\n"
			+ "    acc.listed_at,\r\n"
			+ "    acc.isSold,\r\n"
			+ "    acc.isLocked,\r\n"
			+ "    acc.lockedUntil,\r\n"
			+ "    acc.price,\r\n"
			+ "    acc.account_type,\r\n"
			+ "    img.image_url,\r\n"
			+ "    -- Các cột từ bảng SubInfo\r\n"
			+ "    sub.rank_info,\r\n"
			+ "    sub.vp,\r\n"
			+ "    sub.melee_amount,\r\n"
			+ "    sub.gun_amount,\r\n"
			+ "    sub.btp\r\n"
			+ "FROM AccForSell acc\r\n"
			+ "LEFT JOIN AccountImage img ON acc.account_id = img.account_id\r\n"
			+ "LEFT JOIN SubInfo sub ON acc.account_id = sub.account_id;", nativeQuery = true)
	List<customExportInfoAccAdmin> customExportAccForAdmin();
	
	@Query(value = "select \r\n"
			+ "    acc.account_id,\r\n"
			+ "    acc.listed_at,\r\n"
			+ "    acc.isSold,\r\n"
			+ "    acc.isLocked,\r\n"
			+ "    acc.lockedUntil,\r\n"
			+ "    acc.price,\r\n"
			+ "    acc.account_type,\r\n"
			+ "    img.image_url,\r\n"
			+ "    sub.rank_info,\r\n"
			+ "    sub.vp,\r\n"
			+ "    sub.melee_amount,\r\n"
			+ "    sub.gun_amount,\r\n"
			+ "    sub.btp\r\n"
			+ "from AccForSell acc\r\n"
			+ "left join AccountImage img on acc.account_id = img.account_id\r\n"
			+ "left join SubInfo sub on sub.account_id = acc.account_id\r\n"
			+ "where acc.isSold = 0 \r\n"
			+ "  and acc.isLocked = 0\r\n"
			+ "  and acc.account_type = :type", nativeQuery = true)
	List<customAccInfoForUser> getCustomAccInfoForUser(@Param("type") String type);
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
		String getRank_info();
		Integer getVp();
		Integer getMelee_amount();
		Integer getGun_amount();
		Integer getBtp();
	}
	public interface customExportInfoAccAdmin{
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
		String getRank_info();
		Integer getVp();
		Integer getMelee_amount();
		Integer getGun_amount();
		Integer getBtp();
	}
	public interface customAccInfoForUser{
		Integer getAccount_id();
		LocalDateTime getListed_at();
		boolean getIsSold();
		boolean getIsLocked();
		LocalDateTime getLockedUntil();
		Integer getPrice();
		String getAccount_type();
		String getImage_url();
		String getRank_info();
		Integer getVp();
		Integer getMelee_amount();
		Integer getGun_amount();
		Integer getBtp();
	}
}
