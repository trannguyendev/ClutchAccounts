package com.kingdomeprotocol.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kingdomeprotocol.model.BoughtLogModel;

public interface BoughtLogRepository extends JpaRepository<BoughtLogModel, Integer>{
@Query(value = "select b.bought_id as id, b.bought_time as bought_time, a.email as email_acc, a.username as username_acc, a.account_psw as psw_acc, a.account_type as type_acc, a.price as price from BoughtLog b\r\n"
		+ "inner join Users u on b.user_id = u.id\r\n"
		+ "inner join AccForSell a on b.account_id = a.account_id\r\n"
		+ "where u.email = :email order by b.bought_id desc", nativeQuery = true)
List<customBoughtLog> getSelfBoughtLogs(@Param("email") String email);
interface customBoughtLog{
	Integer getId();
	LocalDateTime getBought_time();
	String getEmail_acc();
	String getUsername_acc();
	String getPsw_acc();
	String getType_acc();
	Integer getPrice();
}
}
