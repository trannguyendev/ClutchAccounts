package com.kingdomeprotocol.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kingdomeprotocol.model.NewsModel;
import com.kingdomeprotocol.service.NewsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/news")
public class NewsController {
	private final NewsService newsService;
	
	// GET all news
	@GetMapping("/info")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAllNews() {
		try {
			return ResponseEntity.ok(newsService.getAllNews());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Map.of("error", e.getMessage()));
		}
	}
	
	// GET news by id
	@GetMapping("/info/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getNewsById(@PathVariable int id) {
		try {
			Optional<NewsModel> news = newsService.getNewsById(id);
			if (news.isPresent()) {
				return ResponseEntity.ok(news.get());
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(Map.of("error", "News not found with id: " + id));
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Map.of("error", e.getMessage()));
		}
	}
	
	// POST create new news
	@PostMapping("/create")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> createNews(@RequestBody Map<String, Object> request) {
		try {
			String title = (String) request.get("news_title");
			String content = (String) request.get("news_content");
			String embedLink = (String) request.get("embed_link");
			int authorId = ((Number) request.get("author_id")).intValue();
			
			if (title == null || title.isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(Map.of("error", "News title is required"));
			}
			if (content == null || content.isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(Map.of("error", "News content is required"));
			}
			
			NewsModel newNews = newsService.createNews(title, content, embedLink, authorId);
			return ResponseEntity.status(HttpStatus.CREATED).body(newNews);
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Map.of("error", e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", e.getMessage()));
		}
	}
	
	// PUT update news
	@PutMapping("/update/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> updateNews(@PathVariable int id, @RequestBody Map<String, Object> request) {
		try {
			String title = (String) request.get("news_title");
			String content = (String) request.get("news_content");
			String embedLink = (String) request.get("embed_link");
			
			NewsModel updatedNews = newsService.updateNews(id, title, content, embedLink);
			return ResponseEntity.ok(updatedNews);
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Map.of("error", e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", e.getMessage()));
		}
	}
	
	// DELETE news
	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteNews(@PathVariable int id) {
		try {
			newsService.deleteNews(id);
			return ResponseEntity.ok(Map.of("message", "News deleted successfully"));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Map.of("error", e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", e.getMessage()));
		}
	}
}
