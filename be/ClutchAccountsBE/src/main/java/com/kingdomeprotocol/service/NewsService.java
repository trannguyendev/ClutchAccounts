package com.kingdomeprotocol.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kingdomeprotocol.model.NewsModel;
import com.kingdomeprotocol.model.UserModel;
import com.kingdomeprotocol.repository.NewsRepository;
import com.kingdomeprotocol.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NewsService {
	private final NewsRepository newsRepo;
	
	private final UserRepository userRepo;
	
	// Get all news
	public List<NewsModel> getAllNews() {
		return newsRepo.findAll();
	}
	
	// Get news by id
	public Optional<NewsModel> getNewsById(int newsId) {
		return newsRepo.findById(newsId);
	}
	
	// Create new news
	public NewsModel createNews(String title, String content, String embedLink, int authorId) {
		Optional<UserModel> author = userRepo.findById(authorId);
		if (!author.isPresent()) {
			throw new RuntimeException("Author not found with id: " + authorId);
		}
		
		NewsModel news = new NewsModel();
		news.setTitle(title);
		news.setContent(content);
		news.setEmbed_link(embedLink);
		news.setCreated_at(LocalDateTime.now());
		news.setAuthor_id(author.get());
		
		return newsRepo.save(news);
	}
	
	// Update existing news
	public NewsModel updateNews(int newsId, String title, String content, String embedLink) {
		Optional<NewsModel> existingNews = newsRepo.findById(newsId);
		if (!existingNews.isPresent()) {
			throw new RuntimeException("News not found with id: " + newsId);
		}
		
		NewsModel news = existingNews.get();
		if (title != null && !title.isEmpty()) {
			news.setTitle(title);
		}
		if (content != null && !content.isEmpty()) {
			news.setContent(content);
		}
		if (embedLink != null && !embedLink.isEmpty()) {
			news.setEmbed_link(embedLink);
		}
		
		return newsRepo.save(news);
	}
	
	// Delete news by id
	public void deleteNews(int newsId) {
		if (!newsRepo.existsById(newsId)) {
			throw new RuntimeException("News not found with id: " + newsId);
		}
		newsRepo.deleteById(newsId);
	}
}

