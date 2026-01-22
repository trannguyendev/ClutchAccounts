package com.kingdomeprotocol.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kingdomeprotocol.model.NewsModel;

@Repository
public interface NewsRepository extends JpaRepository<NewsModel, Integer>{

}
