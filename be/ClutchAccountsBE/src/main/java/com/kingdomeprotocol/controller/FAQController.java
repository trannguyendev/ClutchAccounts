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

import com.kingdomeprotocol.model.FAQModel;
import com.kingdomeprotocol.service.FAQService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/faq")
public class FAQController {
    private final FAQService faqService;

    // GET all FAQs (public)
    @GetMapping("/info")
    public ResponseEntity<?> getAllFAQs() {
        try {
            return ResponseEntity.ok(faqService.getAllFAQs());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // GET FAQ by id (public)
    @GetMapping("/info/{id}")
    public ResponseEntity<?> getFAQById(@PathVariable int id) {
        try {
            Optional<FAQModel> faq = faqService.getFAQById(id);
            if (faq.isPresent()) {
                return ResponseEntity.ok(faq.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "FAQ not found with id: " + id));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // POST create new FAQ
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createFAQ(@RequestBody Map<String, Object> request) {
        try {
            String title = (String) request.get("title");
            String content = (String) request.get("content");

            if (title == null || title.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "FAQ title is required"));
            }
            if (content == null || content.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "FAQ content is required"));
            }

            FAQModel newFAQ = faqService.createFAQ(title, content);
            return ResponseEntity.status(HttpStatus.CREATED).body(newFAQ);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // PUT update FAQ
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateFAQ(@PathVariable int id, @RequestBody Map<String, Object> request) {
        try {
            String title = (String) request.get("title");
            String content = (String) request.get("content");

            FAQModel updatedFAQ = faqService.updateFAQ(id, title, content);
            return ResponseEntity.ok(updatedFAQ);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // DELETE FAQ
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteFAQ(@PathVariable int id) {
        try {
            faqService.deleteFAQ(id);
            return ResponseEntity.ok(Map.of("message", "FAQ deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // POST vote FAQ
    @PostMapping("/vote/{id}")
    public ResponseEntity<?> voteFAQ(@PathVariable int id, @RequestBody Map<String, Object> request) {
        try {
            Boolean isHelpful = (Boolean) request.get("isHelpful");
            if (isHelpful == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "isHelpful is required"));
            }

            FAQModel updatedFAQ = faqService.voteFAQ(id, isHelpful);
            return ResponseEntity.ok(updatedFAQ);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}