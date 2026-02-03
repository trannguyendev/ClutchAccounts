package com.kingdomeprotocol.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kingdomeprotocol.model.FAQModel;

@Repository
public interface FAQRepository extends JpaRepository<FAQModel, Integer> {
}