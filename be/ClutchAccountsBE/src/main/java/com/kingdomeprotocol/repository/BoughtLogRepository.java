package com.kingdomeprotocol.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kingdomeprotocol.model.BoughtLogModel;

public interface BoughtLogRepository extends JpaRepository<BoughtLogModel, Integer>{

}
