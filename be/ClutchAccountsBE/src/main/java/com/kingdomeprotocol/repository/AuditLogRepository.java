package com.kingdomeprotocol.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kingdomeprotocol.model.AuditLogModel;

public interface AuditLogRepository extends JpaRepository<AuditLogModel, Integer>{

	@Query(value = "select logged_ip, logged_device, logged_time from AuditLog where user_id = :id", nativeQuery = true)
	List<AuditLogRes> loadAuditById(@Param("id") int id);
	interface AuditLogRes{
		String getLogged_ip();
		String getLogged_device();
		LocalDateTime getLogged_time();
	}
}
