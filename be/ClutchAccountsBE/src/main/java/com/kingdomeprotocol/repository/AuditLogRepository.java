package com.kingdomeprotocol.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kingdomeprotocol.model.AuditLogModel;

public interface AuditLogRepository extends JpaRepository<AuditLogModel, Integer>{

}
