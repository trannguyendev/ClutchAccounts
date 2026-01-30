package com.kingdomeprotocol.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kingdomeprotocol.model.NewsModel;

@Repository
public interface NewsRepository extends JpaRepository<NewsModel, Integer>{
	@Query(value = "select news_id as newsId, title, content, embed_link as embedLink, created_at as createdAt, author_id as authorId from News", nativeQuery = true)
	List<CustomNews> getAllNews();
	
	interface CustomNews{
		Integer getNewsId();
		String getTitle();
		String getContent();
		String getEmbedLink();
		LocalDateTime getCreatedAt();
		Integer getAuthorId();
	}
}
