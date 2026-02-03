package com.kingdomeprotocol.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.kingdomeprotocol.model.FAQModel;
import com.kingdomeprotocol.repository.FAQRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FAQService {
    private final FAQRepository faqRepo;

    // Get all FAQs
    public List<FAQModel> getAllFAQs() {
        return faqRepo.findAll();
    }

    // Get FAQ by id
    public Optional<FAQModel> getFAQById(int faqId) {
        return faqRepo.findById(faqId);
    }

    // Create new FAQ
    public FAQModel createFAQ(String title, String content) {
        FAQModel faq = new FAQModel();
        faq.setTitle(title);
        faq.setContent(content);
        faq.setCreated_at(LocalDateTime.now());
        faq.setUpdated_at(LocalDateTime.now());
        faq.setLike_amount(0);
        faq.setDislike_amount(0);

        return faqRepo.save(faq);
    }

    // Update existing FAQ
    public FAQModel updateFAQ(int faqId, String title, String content) {
        Optional<FAQModel> existingFAQ = faqRepo.findById(faqId);
        if (!existingFAQ.isPresent()) {
            throw new RuntimeException("FAQ not found with id: " + faqId);
        }

        FAQModel faq = existingFAQ.get();
        if (title != null && !title.isEmpty()) {
            faq.setTitle(title);
        }
        if (content != null && !content.isEmpty()) {
            faq.setContent(content);
        }
        faq.setUpdated_at(LocalDateTime.now());

        return faqRepo.save(faq);
    }

    // Delete FAQ by id
    public void deleteFAQ(int faqId) {
        if (!faqRepo.existsById(faqId)) {
            throw new RuntimeException("FAQ not found with id: " + faqId);
        }
        faqRepo.deleteById(faqId);
    }

    // Vote helpful
    public FAQModel voteFAQ(int faqId, boolean isHelpful) {
        Optional<FAQModel> existingFAQ = faqRepo.findById(faqId);
        if (!existingFAQ.isPresent()) {
            throw new RuntimeException("FAQ not found with id: " + faqId);
        }

        FAQModel faq = existingFAQ.get();
        if (isHelpful) {
            faq.setLike_amount(faq.getLike_amount() + 1);
        } else {
            faq.setDislike_amount(faq.getDislike_amount() + 1);
        }

        return faqRepo.save(faq);
    }
}